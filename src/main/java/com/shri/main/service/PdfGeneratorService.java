package com.shri.main.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.shri.main.model.Student;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    public byte[] generateFeesReceiptPdf(Student student) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();

        // === Header Branding ===
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1f, 4f});
        headerTable.setSpacingAfter(10f);

        Image logo = Image.getInstance(new ClassPathResource("static/images/fgimage.jpeg").getURL());
        logo.scaleToFit(70, 70);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Font brandFont = new Font(Font.FontFamily.TIMES_ROMAN, 26, Font.BOLD, new BaseColor(0, 70, 127));
        Chunk brandChunk = new Chunk("FG INFOTECH (IT CLASSES)", brandFont);
        brandChunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 0.5f, BaseColor.ORANGE);
        Paragraph brandPara = new Paragraph(brandChunk);
        brandPara.setAlignment(Element.ALIGN_LEFT);

        PdfPCell brandCell = new PdfPCell(brandPara);
        brandCell.setBorder(Rectangle.NO_BORDER);
        brandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerTable.addCell(logoCell);
        headerTable.addCell(brandCell);

        document.add(headerTable);

        // === Watermark ===
        PdfContentByte background = writer.getDirectContentUnder();
        Font watermarkFont = new Font(Font.FontFamily.HELVETICA, 80, Font.BOLD, new BaseColor(220, 220, 220, 80));
        Phrase watermark = new Phrase("FG Infotech", watermarkFont);
        ColumnText.showTextAligned(background, Element.ALIGN_CENTER, watermark, 297.5f, 421, 45);

        // === Border ===
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle border = new Rectangle(document.left() - 5, document.bottom() - 5, document.right() + 5, document.top() + 5);
        border.setBorder(Rectangle.BOX);
        border.setBorderWidth(2f);
        border.setBorderColor(BaseColor.GRAY);
        canvas.rectangle(border);

        // === Registration No ===
        Font regFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 102, 102));
        Paragraph regPara = new Paragraph("Registration No: 2520700320230021", regFont);
        regPara.setAlignment(Element.ALIGN_RIGHT);
        regPara.setSpacingAfter(5f);
        document.add(regPara);

        // === Receipt Number ===
        Font receiptFont = new Font(Font.FontFamily.COURIER, 13, Font.BOLDITALIC, BaseColor.BLACK);
        Paragraph receiptNumber = new Paragraph("Receipt No: FG-" + student.getId(), receiptFont);
        receiptNumber.setAlignment(Element.ALIGN_RIGHT);
        receiptNumber.setSpacingAfter(10f);
        document.add(receiptNumber);

        // === Title ===
        Paragraph title = new Paragraph("FEES RECEIPT", new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, new BaseColor(0, 70, 127)));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(5f);
        document.add(title);

        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.GRAY);
        document.add(line);

        // === Student Info Table ===
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{1f, 2f});
        infoTable.setSpacingBefore(10f);

        Font infoFont = new Font(Font.FontFamily.HELVETICA, 11);
        infoTable.addCell(getStudentInfoCell("Name:", infoFont));
        infoTable.addCell(getStudentInfoCell(student.getName(), infoFont));
        infoTable.addCell(getStudentInfoCell("Batch No:", infoFont));
        infoTable.addCell(getStudentInfoCell(student.getBatchNo(), infoFont));
        
        infoTable.addCell(getStudentInfoCell("Admission Date:", infoFont));
        String formattedDate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(student.getAdmissionDate());
        infoTable.addCell(getStudentInfoCell(formattedDate, infoFont));

        document.add(infoTable);

        // === Fees Table ===
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(20f);

        BaseColor headerColor = new BaseColor(230, 230, 250);

        table.addCell(getStyledHeaderCell("Total Fees", headerColor));
        table.addCell(getStyledHeaderCell("Received Fees", headerColor));
        table.addCell(getStyledHeaderCell("Pending Fees", headerColor));

        table.addCell(getCell(String.format("%.2f", student.getTotalFees()), Element.ALIGN_CENTER, Font.NORMAL));
        table.addCell(getCell(String.format("%.2f", student.getPaidFees()), Element.ALIGN_CENTER, Font.NORMAL));
        table.addCell(getCell(String.format("%.2f", student.getTotalFees() - student.getPaidFees()), Element.ALIGN_CENTER, Font.NORMAL));

        document.add(table);

        // === Info Text ===
        Font infoTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Paragraph infoText = new Paragraph("We are pleased to confirm that your full fees have been successfully paid. " +
                "This receipt serves as an official acknowledgment of your payment. " +
                "If you have any questions or concerns, please contact our office at 9511200293.", infoTextFont);
        infoText.setSpacingAfter(20f);
        document.add(infoText);

        // === Signature ===
        Paragraph signLabel = new Paragraph("Authorized Signature", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
        signLabel.setAlignment(Element.ALIGN_RIGHT);
        document.add(signLabel);

        BaseFont baseFont = BaseFont.createFont("fonts/GreatVibes-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font signFont = new Font(baseFont, 22);
        Paragraph signature = new Paragraph("Shrikant Shinde", signFont);
        signature.setAlignment(Element.ALIGN_RIGHT);
        signature.setSpacingBefore(10f);
        document.add(signature);

        // === Date Time ===
        String dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")
                .withZone(ZoneId.of("Asia/Kolkata")).format(Instant.now());
        Paragraph datePara = new Paragraph("Date: " + dateTime,
                new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL));
        datePara.setAlignment(Element.ALIGN_RIGHT);
        document.add(datePara);

        // === Note Section ===
        PdfPTable relatedInfoTable = new PdfPTable(1);
        relatedInfoTable.setWidthPercentage(100);
        relatedInfoTable.setSpacingBefore(20f);

        String relatedText = "Please keep this receipt for future reference.\n" +
                "This receipt is generated electronically and does not require a physical signature.\n" +
                "This receipt confirms that the fee has been received by FG Infotech.";

        PdfPCell notesCell = getNoteCell(relatedText);
        notesCell.setBorder(Rectangle.BOX);
        notesCell.setBorderColor(BaseColor.LIGHT_GRAY);
        notesCell.setBackgroundColor(new BaseColor(255, 255, 230));
        relatedInfoTable.addCell(notesCell);
        document.add(relatedInfoTable);

        // === Footer ===
        Paragraph footer = new Paragraph();
        footer.setSpacingBefore(30f);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.add(new Phrase("Thanks & Regards,\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        footer.add(new Phrase("FG Infotech\n", new Font(Font.FontFamily.HELVETICA, 11)));
        footer.add(new Phrase("Website: https://fginfotech.in", new Font(Font.FontFamily.HELVETICA, 10, Font.UNDERLINE, BaseColor.BLUE)));
        document.add(footer);

        document.close();
        return out.toByteArray();
    }

    private PdfPCell getStudentInfoCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        return cell;
    }

    private PdfPCell getStyledHeaderCell(String text, BaseColor bgColor) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        return cell;
    }

    private PdfPCell getCell(String text, int alignment, int style) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, style);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(8);
        return cell;
    }

    private PdfPCell getNoteCell(String text) {
        Font headingFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        Phrase phrase = new Phrase("Note:\n", headingFont);

        Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY);
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                phrase.add(new Chunk("\u2022 " + line.trim() + "\n", bodyFont));
            }
        }

        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(10f);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}