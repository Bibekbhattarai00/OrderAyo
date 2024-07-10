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
import com.example.summerproject.repo.OrderItemsRepo;
import com.example.summerproject.repo.OrderRepo;
import com.example.summerproject.repo.ProductRepo;
import com.example.summerproject.service.OrdersService;
import com.example.summerproject.service.ProductService;
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
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.example.summerproject.utils.NullValues.getNullPropertyNames;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrdersService {

    private final OrderRepo orderRepo;
    private final ObjectMapper objectMapper;
    private final ProductRepo productRepo;
    private final CustomMessageSource messageSource;
    private final OrderMapper orderMapper;
    private final MailUtils mailUtils;
    private final OrderItemsRepo orderItemsRepo;
    private final ProductService productService;
    private Logger logger;


    @Override
    public String placeOrder(OrderDto orderDto) {
        if (orderDto.getOrderId() != null) {
            OrderEntity orderById = orderRepo.findById(orderDto.getOrderId())
                    .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));

            List<OrderItemDto> orderItems = orderDto.getOrderItems();
            List<OrderItem> existingOrderItems = orderById.getOrderItems();
            List<OrderItem> updatedOrderItem =new ArrayList<>();

            //reset previous stock
            for(OrderItem existingItems:existingOrderItems){
                Product product = existingItems.getProduct();
                Long quantity = existingItems.getQuantity();
                product.setStock(product.getStock()+quantity);
                productRepo.save(product);
            }

            for(OrderItemDto orderItemDto:orderItems) {

                OrderItem orderItem = orderItemsRepo.findById(orderItemDto.getId())
                        .orElseThrow(()->new NotFoundException("Order item does not Exist"));
                BeanUtils.copyProperties(orderItemDto, orderItem, getNullPropertyNames(orderDto.getOrderItems()));

                Product product = productRepo.findById(orderItemDto.getProductId()).orElseThrow(() -> new NotFoundException("product not found"));
                Long quantity = orderItemDto.getQuantity();

                if (product.isDeleted() || product.getStock() < quantity) {
                    throw new NotFoundException("Insufficient stock for product: " + product.getName());
                }

                product.setStock(product.getStock() - quantity);

                if(product.getStock()==quantity){
//                    product.setDeleted(true);
                    productService.deleteProduct(product.getId());
                }
                orderItem.setProduct(product);
                updatedOrderItem.add(orderItem);
//                productRepo.save(product);
                orderItemsRepo.save(orderItem);
            }

            BeanUtils.copyProperties(orderDto, orderById, getNullPropertyNames(orderDto));
            orderById.setOrderItems(updatedOrderItem);
            orderRepo.save(orderById);
            return messageSource.get(ExceptionMessages.UPDATE.getCode());

        }
        List<OrderItemDto> orderItemDtos = orderDto.getOrderItems();
        List<OrderItem> orderItems = new ArrayList<>();

        OrderEntity order = new OrderEntity();
        order.setCustomerName(orderDto.getCustomerName());
        order.setCustomerContact(orderDto.getCustomerContact());
        order.setCustomerEmail(orderDto.getCustomerEmail());
        order.setAddress(orderDto.getAddress());
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
            if(product.getStock()==quantity){
                productService.deleteProduct(product.getId());
//                product.setDeleted(true);
            }
//            productRepo.save(product);

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
    public OrderResponseDto viewPendingOrdersById(Long id) {
        return orderMapper.getOrderDetailsById(id);
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
        if (order.getCustomerEmail() != null) {
            String emailBody = "Dear " + order.getCustomerName() + " " +
                    "We're thrilled to inform you that your recent order with us has been dispatched and is on its way to you! Your satisfaction is our top priority, and we're committed to ensuring a smooth delivery experience for you.\n" +
                    "\n" +
                    "To track your order and stay updated on its status, you can now reach out to our trusted courier service, Nepal can Move, directly. They'll be happy to assist you with any inquiries you may have regarding the delivery process.\n" +
                    "\n" +
                    "Please provide them with your name and contact information, and they'll promptly provide you with the necessary details about your order's journey to your doorstep.\n" +
                    "\n" +
                    "Nepal Can Move\n" +
                    "Phone: 9800000000\n" +
                    "Email: Ncm123@gmail.com\n" +
                    "\n" +
                    "Should you have any further questions or concerns, feel free to reach out to us directly, and we'll be more than happy to assist you.\n" +
                    "\n" +
                    "Thank you for choosing us for your feet needs. We appreciate your trust in us and hope you enjoy your purchase!\n" +
                    "\n" +
                    "Best regards,\n" +
                    "\n" +
                    "Regina Chapagain\n" +
                    "9811111111\n" +
                    "Regina Shoe Store";
            mailUtils.sendMail(order.getCustomerEmail(), "Order dispatched Notification", emailBody);
        }
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

        // Use ByteArrayOutputStream instead of FileOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
        Phrase orgName = new Phrase("Regina Shoe Store \n Urlabari-7 Morang", font);
        document.add(orgName);

        Font customerFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Phrase customerInfo = new Phrase("\nCustomer Name: " + order.getCustomerName() + " Contact: " + order.getCustomerContact(), customerFont);
        document.add(customerInfo);

        PdfPTable table = new PdfPTable(4);
        addTableHeader(table);
        for (ProdnameResponseDto product : order.getProducts()) {
            addBillItem(table, product.getProductName(), String.valueOf(product.getQuantity()), String.valueOf(product.getPrice()), String.valueOf(product.getQuantity() * product.getPrice()), customerFont);
        }

        document.add(table);
        document.close();

        // Convert ByteArrayOutputStream content to byte array
        byte[] pdfBytes = baos.toByteArray();
        // Encode byte array to Base64
        String base64EncodedPDF = Base64.getEncoder().encodeToString(pdfBytes);

        // Set content type to indicate Base64 data
        response.setContentType("application/json");
        // Return Base64 encoded PDF string
        return base64EncodedPDF;
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
//    @Scheduled(cron = "0 * * * * *")
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
    public List<Map<String, Object>> getBestSellers(DateRequestDto dateRequestDto) {
        return orderRepo.getBestSeller(dateRequestDto.getFrom(), dateRequestDto.getTo());
    }


}
