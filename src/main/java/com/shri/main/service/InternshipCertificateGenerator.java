package com.shri.main.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.shri.main.model.Student;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class InternshipCertificateGenerator {

    public byte[] generateInternshipCertificate(Student student, Date fromDate, Date toDate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document(PageSize.A4, 50, 50, 90, 60);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // Background color
            PdfContentByte background = writer.getDirectContentUnder();
            Rectangle pageSize = document.getPageSize();
            background.setColorFill(new BaseColor(255, 253, 240));
            background.rectangle(pageSize.getLeft(), pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight());
            background.fill();

            // Border
            PdfContentByte border = writer.getDirectContent();
            Rectangle rect = new Rectangle(40, 40, pageSize.getRight() - 40, pageSize.getTop() - 40);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(4);
            rect.setBorderColor(new BaseColor(218, 165, 32));
            border.rectangle(rect);

            // Watermark
            Font watermarkFont = new Font(Font.FontFamily.HELVETICA, 70, Font.BOLD, new BaseColor(200, 200, 200, 40));
            ColumnText.showTextAligned(background, Element.ALIGN_CENTER, new Phrase("FG Infotech", watermarkFont), 300, 400, 45);

            // Registration No
            Font regFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.RED);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
                    new Phrase("REGISTRATION NO: 2520700320230091", regFont), document.right() - 10, document.top() + 20, 0);

            // Header
            PdfPTable headerBox = new PdfPTable(2);
            headerBox.setWidthPercentage(100);
            headerBox.setSpacingBefore(10f);
            headerBox.setWidths(new float[]{1.5f, 6.5f});
            BaseColor headerBgColor = new BaseColor(255, 239, 210);

            Image logo = Image.getInstance(new ClassPathResource("static/images/fgimage.jpeg").getURL());
            logo.scaleToFit(55, 55);
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.BOX);
            logoCell.setBorderColor(BaseColor.DARK_GRAY);
            logoCell.setPadding(6f);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setBackgroundColor(headerBgColor);
            headerBox.addCell(logoCell);

            Font brandFont = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD, new BaseColor(0, 102, 204));
            Chunk brandChunk = new Chunk("FG INFOTECH INSTITUTE", brandFont);
            brandChunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 0.5f, BaseColor.ORANGE);
            Paragraph brandPara = new Paragraph(brandChunk);
            brandPara.setAlignment(Element.ALIGN_CENTER);
            brandPara.setSpacingBefore(5f);
            brandPara.setSpacingAfter(5f);
            PdfPCell brandCell = new PdfPCell(brandPara);
            brandCell.setBorder(Rectangle.BOX);
            brandCell.setBorderColor(BaseColor.DARK_GRAY);
            brandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            brandCell.setBackgroundColor(headerBgColor);
            brandCell.setPadding(10f);
            headerBox.addCell(brandCell);

            document.add(headerBox);

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD, new BaseColor(0, 70, 127));
            Paragraph title = new Paragraph("INTERNSHIP COMPLETION CERTIFICATE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(20);
            title.setSpacingAfter(20);
            document.add(title);

            // Certificate No
            Font italicSmall = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, BaseColor.BLACK);
            String certNo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Paragraph certNum = new Paragraph("Certificate No:- " + certNo, italicSmall);
            certNum.setAlignment(Element.ALIGN_RIGHT);
            document.add(certNum);

            // Format Dates
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            String from = sdf.format(fromDate);
            String to = sdf.format(toDate);

            // Certificate Body
            Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK);
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph body = new Paragraph();
            body.setAlignment(Element.ALIGN_JUSTIFIED);
            body.setSpacingBefore(30);
            body.setSpacingAfter(20);
            body.add(new Phrase("This is to certify that ", textFont));
            body.add(new Phrase(student.getName(), boldFont));
            body.add(new Phrase(" has successfully completed their internship as part of the ", textFont));
            body.add(new Phrase(student.getCourse(), boldFont));
            body.add(new Phrase(" program at ", textFont));
            body.add(new Phrase("FG Infotech", boldFont));
            body.add(new Phrase(" from ", textFont));
            body.add(new Phrase(from, boldFont));
            body.add(new Phrase(" to ", textFont));
            body.add(new Phrase(to + ". ", boldFont));
            body.add(new Phrase("During this period, the intern was exposed to real-time projects, demonstrated consistent learning, adaptability, and professional behavior. Their contribution to tasks and assignments was commendable.", textFont));
            document.add(body);

            Paragraph note = new Paragraph("We appreciate their dedication and professionalism and wish them great success in future endeavors.", textFont);
            note.setAlignment(Element.ALIGN_CENTER);
            note.setSpacingAfter(100);
            document.add(note);

         // === Signatures (3 Persons: Left, Center, Right) ===
            PdfPTable signTable = new PdfPTable(3); // Changed to 3 columns
            signTable.setWidthPercentage(100);
            signTable.setSpacingBefore(10f);
            signTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Font signNameFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);

            // === Chandrakant (Left) ===
            Image sig1 = Image.getInstance(new ClassPathResource("static/images/ChandrakantSSign.png").getURL());
            sig1.scaleToFit(40, 15);
            PdfPCell sig1ImageCell = new PdfPCell(sig1, true);
            sig1ImageCell.setBorder(Rectangle.NO_BORDER);
            sig1ImageCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            sig1ImageCell.setFixedHeight(25f);

            PdfPCell sig1NameCell = new PdfPCell(new Phrase("Chandrakant Shinde\nDirector, FG Infotech", signNameFont));
            sig1NameCell.setBorder(Rectangle.NO_BORDER);
            sig1NameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            sig1NameCell.setVerticalAlignment(Element.ALIGN_TOP);
            sig1NameCell.setFixedHeight(25f);

            PdfPTable leftSignature = new PdfPTable(1);
            leftSignature.setWidthPercentage(100);
            leftSignature.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            leftSignature.addCell(sig1ImageCell);
            leftSignature.addCell(sig1NameCell);

            // === Hemant Kawar (Center) ===
            Image sig3 = Image.getInstance(new ClassPathResource("static/images/HemantSSSign.png").getURL());
            sig3.scaleToFit(40, 15);
            PdfPCell sig3ImageCell = new PdfPCell(sig3, true);
            sig3ImageCell.setBorder(Rectangle.NO_BORDER);
            sig3ImageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sig3ImageCell.setFixedHeight(25f);

            PdfPCell sig3NameCell = new PdfPCell(new Phrase("Hemant Kawar\nInternship Supervisor, FG Infotech", signNameFont));
            sig3NameCell.setBorder(Rectangle.NO_BORDER);
            sig3NameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sig3NameCell.setVerticalAlignment(Element.ALIGN_TOP);
            sig3NameCell.setFixedHeight(25f);

            PdfPTable centerSignature = new PdfPTable(1);
            centerSignature.setWidthPercentage(100);
            centerSignature.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            centerSignature.addCell(sig3ImageCell);
            centerSignature.addCell(sig3NameCell);

            // === Shrikant (Right) ===
            Image sig2 = Image.getInstance(new ClassPathResource("static/images/ShrikantSSSign.png").getURL());
            sig2.scaleToFit(40, 15);
            PdfPCell sig2ImageCell = new PdfPCell(sig2, true);
            sig2ImageCell.setBorder(Rectangle.NO_BORDER);
            sig2ImageCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sig2ImageCell.setFixedHeight(25f);

            PdfPCell sig2NameCell = new PdfPCell(new Phrase("Shrikant Shinde\nTrainer, FG Infotech", signNameFont));
            sig2NameCell.setBorder(Rectangle.NO_BORDER);
            sig2NameCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sig2NameCell.setVerticalAlignment(Element.ALIGN_TOP);
            sig2NameCell.setFixedHeight(25f);

            PdfPTable rightSignature = new PdfPTable(1);
            rightSignature.setWidthPercentage(100);
            rightSignature.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            rightSignature.addCell(sig2ImageCell);
            rightSignature.addCell(sig2NameCell);

            // === Wrap Each Column into SignTable ===
            PdfPCell leftWrapper = new PdfPCell(leftSignature);
            leftWrapper.setBorder(Rectangle.NO_BORDER);

            PdfPCell centerWrapper = new PdfPCell(centerSignature);
            centerWrapper.setBorder(Rectangle.NO_BORDER);

            PdfPCell rightWrapper = new PdfPCell(rightSignature);
            rightWrapper.setBorder(Rectangle.NO_BORDER);

            signTable.addCell(leftWrapper);
            signTable.addCell(centerWrapper);
            signTable.addCell(rightWrapper);

            document.add(signTable);

            // Issue Date
            Paragraph footerDate = new Paragraph("Issued on: " + new SimpleDateFormat("dd MMM yyyy hh:mm a").format(new Date()),
                    new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.DARK_GRAY));
            footerDate.setAlignment(Element.ALIGN_LEFT);
            footerDate.setSpacingBefore(15f);
            footerDate.setSpacingAfter(8f);
            document.add(footerDate);

            // QR Code
            String qrText = "FG Infotech Internship Verification ✅\n\n"
                    + "Intern: " + student.getName() + "\n"
                    + "Department: " + student.getCourse() + "\n"
                    + "Duration: " + from + " – " + to + "\n"
                    + "Certificate No: " + certNo + "\n\n"
                    + "✅ This intern has successfully completed internship at FG Infotech.";

            Font qrMsgFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph qrMsg = new Paragraph("Scan to validate certificate", qrMsgFont);
            qrMsg.setAlignment(Element.ALIGN_CENTER);
            qrMsg.setSpacingBefore(10f);
            qrMsg.setSpacingAfter(2f);
            document.add(qrMsg);

            Image qrImage = generateQRCode(qrText, 50, 50);
            qrImage.scaleAbsolute(50, 50);
            qrImage.setAlignment(Image.ALIGN_CENTER);
            qrImage.setSpacingAfter(5f);
            document.add(qrImage);

            // Footer
            PdfPTable footerBox = new PdfPTable(1);
            footerBox.setTotalWidth(PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
            footerBox.setLockedWidth(true);
            footerBox.setHorizontalAlignment(Element.ALIGN_CENTER);
            BaseColor footerColor = new BaseColor(240, 240, 255);
            Font addressFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

            PdfPCell footerCell = new PdfPCell(new Phrase(
                    "Address: FG Infotech, Online IT Training Institute, Pune & Mumbai\nWebsite: https://fginfotech.in | Email: future.growing.infotech@gmail.com",
                    addressFont));
            footerCell.setBackgroundColor(footerColor);
            footerCell.setBorder(Rectangle.TOP);
            footerCell.setBorderColor(BaseColor.GRAY);
            footerCell.setPaddingTop(6f);
            footerCell.setPaddingBottom(6f);
            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footerBox.addCell(footerCell);

            document.add(footerBox);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    private Image generateQRCode(String qrText, int width, int height) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0x00FFFFFF);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", out, config);
        return Image.getInstance(out.toByteArray());
    }
}
