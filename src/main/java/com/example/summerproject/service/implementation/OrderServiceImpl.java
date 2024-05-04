package com.example.summerproject.service.implementation;

import com.example.summerproject.dto.request.DateRequestDto;
import com.example.summerproject.dto.request.OrderDto;
import com.example.summerproject.dto.request.OrderItemDto;
import com.example.summerproject.dto.response.OrderResponseDto;
import com.example.summerproject.dto.response.ProdnameResponseDto;
import com.example.summerproject.entity.OrderEntity;
import com.example.summerproject.entity.OrderItem;
import com.example.summerproject.entity.Product;
import com.example.summerproject.enums.OrderStatus;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.mapper.OrderMapper;
import com.example.summerproject.repo.OrderRepo;
import com.example.summerproject.repo.ProductRepo;
import com.example.summerproject.service.OrdersService;
import com.example.summerproject.utils.MailUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrdersService {

    private final OrderRepo orderRepo;
    private final ObjectMapper objectMapper;
    private final ProductRepo productRepo;
    private final CustomMessageSource messageSource;
    private final OrderMapper orderMapper;
    private final MailUtils mailUtils;
    private Logger logger;

    @Override
    public String placeOrder(OrderDto orderDto) {
        List<OrderItemDto> orderItemDtos = orderDto.getOrderItems();
        List<OrderItem> orderItems = new ArrayList<>();

        OrderEntity order = new OrderEntity();
        order.setCustomerName(orderDto.getCustomerName());
        order.setCustomerContact(orderDto.getCustomerContact());
        order.setOrderStatus(OrderStatus.PENDING);

        for (OrderItemDto orderItemDto : orderItemDtos) {
            Long productId = orderItemDto.getProductId();
            Long quantity = orderItemDto.getQuantity();

            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));

            if (product.isDeleted() || product.getStock() < quantity) {
                throw new NotFoundException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - quantity);
            productRepo.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        orderRepo.save(order);

        return messageSource.get(ExceptionMessages.SUCCESS.getCode());
    }


    @Override
    public List<OrderResponseDto> viewPendingOrders() {
        return orderMapper.getOrdersWithProducts();
    }

    @Override
    public List<OrderResponseDto> viewOrderHistory() {
        return orderMapper.getOrderHistory();
    }

    @Override
    public String dispatchOrder(Long id) {
        OrderEntity order = orderRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
        order.setOrderStatus(OrderStatus.DISPATCHED);
        order.setDeleted(true);
        orderRepo.save(order);
        return messageSource.get(ExceptionMessages.SUCCESS.getCode());
    }

    @Override
    public List<OrderResponseDto> viewOrderByCustomer(String phone) {
        return orderMapper.getOrdersWithProductsByCustomer(phone);
    }

    @Override
    public OrderResponseDto getOrderDetailsById(Long orderId) {
        return orderMapper.getOrderDetailsById(orderId);
    }


    public String generateBill(Long orderId, HttpServletResponse response) throws IOException, DocumentException {


        OrderResponseDto order = getOrderDetailsById(orderId);

        OrderEntity byId = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("bill_order_" + order.getOrderId() + ".pdf"));

        document.open();


        Font Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
        Phrase orgName = new Phrase("Regina Shoe Store \n Urlabari-7 Morang", Font);
        document.add(orgName);

        Font customerFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Phrase customerInfo = new Phrase("\nCustomer Name: " + order.getCustomerName() + "Contact: " + order.getCustomerContact(), customerFont);
        document.add(customerInfo);


        PdfPTable table = new PdfPTable(4);
        addTableHeader(table);
        for (ProdnameResponseDto product : order.getProducts()) {
            addBillItem(table, product.getProductName(), String.valueOf(product.getQuantity()), String.valueOf(product.getPrice()), String.valueOf(product.getQuantity() * product.getPrice()), customerFont);
        }

        document.add(table);
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=bill_order.pdf";
        response.setHeader(headerKey, headerValue);
        document.close();

        return messageSource.get(ExceptionMessages.DOWNLOADED.getCode());
    }

    private static void addTableHeader(PdfPTable table) {
        Stream.of("Item", "Qty", "Price", "Amount")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private static void addBillItem(PdfPTable table, String item, String qty, String price, String amount, Font font) {
        PdfPCell itemCell = new PdfPCell(new Phrase(item, font));
        itemCell.setBorderWidth(1);
        table.addCell(itemCell);

        PdfPCell qtyCell = new PdfPCell(new Phrase(qty, font));
        qtyCell.setBorderWidth(1);
        table.addCell(qtyCell);

        PdfPCell priceCell = new PdfPCell(new Phrase(price, font));
        priceCell.setBorderWidth(1);
        table.addCell(priceCell);

        PdfPCell amountCell = new PdfPCell(new Phrase(amount, font));
        amountCell.setBorderWidth(1);
        table.addCell(amountCell);
    }

    @Scheduled(cron = "0 30 20 * * *")
//    @Scheduled(fixedRate = 500)
    public void sendPendingOrderReminderEmail() {
        Optional<List<OrderEntity>> pendingOrders = orderRepo.findByOrderStatus(OrderStatus.PENDING);

        if (pendingOrders.isPresent()) {
            List<OrderEntity> orders = pendingOrders.get();

            for (OrderEntity order : orders) {
                Date createdDate = order.getCreatedDate();
                if (createdDate != null && is24HoursPassed(createdDate) && order.getOrderStatus() == OrderStatus.PENDING) {
                    String emailBody = "Your order created on " + createdDate + " is still pending. Please take action.";
                    mailUtils.sendMail("bibek@yopmail.com", "Pending Order Reminder", emailBody);
                }
            }

        } else {
            logger.log(logger.getLevel(), "NO pending Orders");
        }
    }

    private boolean is24HoursPassed(Date createdDate) {
        LocalDateTime createdDateTime = createdDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(createdDateTime, now).toHours() > 24;
    }

    @Override
    public String getExcel(HttpServletResponse response) throws IOException, IllegalAccessException {
//        ExcelGenerator.generateExcel(response,orderMapper.getOrderHistory(),"author sheet", OrderResponseDto.class);
        List<OrderResponseDto> orderHistory = orderMapper.getOrderHistory();

        HSSFWorkbook workbook = new HSSFWorkbook();

        //create Sheet
        HSSFSheet sheet = workbook.createSheet("Order history details");

        //create Row
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("Customer Name");
        row.createCell(2).setCellValue("Customer Contact");
        row.createCell(3).setCellValue("Product name");
        row.createCell(4).setCellValue("Quantity");
        row.createCell(5).setCellValue("price");
        row.createCell(6).setCellValue("total");

        //since index 0 has headers so data index start from 1
        int dataRow = 1;
        for (OrderResponseDto orders : orderHistory) {
            //insert data to excelSheet
            HSSFRow data = sheet.createRow(dataRow);
            data.createCell(0).setCellValue(orders.getOrderId());
            data.createCell(1).setCellValue(orders.getCustomerName());
            data.createCell(2).setCellValue(orders.getCustomerContact());
            List<ProdnameResponseDto> products = orders.getProducts();
            for (ProdnameResponseDto prod : products) {
                data.createCell(3).setCellValue(prod.getProductName());
                data.createCell(4).setCellValue(prod.getQuantity());
                data.createCell(5).setCellValue(prod.getPrice());
            }
            data.createCell(6).setCellValue(orders.getTotal());
            dataRow++;
        }

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=transactions.xls";
        response.setHeader(headerKey, headerValue);
        out.close();
        return messageSource.get(ExceptionMessages.DOWNLOADED.getCode());
    }

    @Override
    public Map<String, Object> getSalesReport(DateRequestDto dateRequestDto) {
        return orderRepo.getSalesReport(dateRequestDto.getFrom(), dateRequestDto.getTo());
    }

    @Override
    public Map<String, Object> getBestSellers(DateRequestDto dateRequestDto) {
        return orderRepo.getBestSeller(dateRequestDto.getFrom(), dateRequestDto.getTo());
    }


}
