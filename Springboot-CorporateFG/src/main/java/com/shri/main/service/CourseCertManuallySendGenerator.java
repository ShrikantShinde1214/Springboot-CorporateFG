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
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class CourseCertManuallySendGenerator {

	public byte[] generateCourseCompletionCertificate(Student student, Date fromDate, Date toDate) throws Exception {
	

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 90, 60); // Adjusted internal margin
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // === Background Color ===
        PdfContentByte background = writer.getDirectContentUnder();
        Rectangle pageSize = document.getPageSize();
        background.setColorFill(new BaseColor(250, 250, 240)); // soft ivory
        background.rectangle(pageSize.getLeft(), pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight());
        background.fill();

        // === Border ===
        PdfContentByte border = writer.getDirectContent();
        Rectangle rect = new Rectangle(40, 40, pageSize.getRight() - 40, pageSize.getTop() - 40);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(4);
        rect.setBorderColor(new BaseColor(0, 70, 127));
        border.rectangle(rect);

        // === Watermark ===
        Font watermarkFont = new Font(Font.FontFamily.HELVETICA, 70, Font.BOLD, new BaseColor(200, 200, 200, 40));
        ColumnText.showTextAligned(background, Element.ALIGN_CENTER,
                new Phrase("FG Infotech", watermarkFont), 300, 400, 45);
        
     // === REGISTRATION NO (Top Right Corner, Outside Header Box) ===
        Font regFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.RED);
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_RIGHT,
                new Phrase("REGISTRATION NO: 2520700320230021", regFont),
                document.right() - 10, // 10 points from right edge
                document.top() + 20,   // Slightly above the top margin
                0); // no rotation
     

     // === Header Table with 2 Columns: Logo | FG INFOTECH
        PdfPTable headerBox = new PdfPTable(2);
        headerBox.setWidthPercentage(100);
        headerBox.setWidths(new float[]{1.2f, 6.8f}); // Logo takes less space, name takes more

        BaseColor headerBgColor = new BaseColor(220, 235, 245); // Soft certificate-blue

        // === Logo Cell (Top-Left)
        Image logo = Image.getInstance(new ClassPathResource("static/images/fgimage.jpeg").getURL());
        logo.scaleToFit(55, 55);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.BOX);
        logoCell.setBorderColor(BaseColor.DARK_GRAY);
        logoCell.setBackgroundColor(headerBgColor);
        logoCell.setPadding(6f);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerBox.addCell(logoCell);

        // === FG INFOTECH Brand Cell (Centered)
        Font brandFont = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD, new BaseColor(0, 102, 204)); // Blue fill
        Chunk brandChunk = new Chunk("FG INFOTECH", brandFont);
        brandChunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 0.5f, BaseColor.RED); // Red outline

        Paragraph brandPara = new Paragraph(brandChunk);
        brandPara.setAlignment(Element.ALIGN_CENTER);
        brandPara.setSpacingBefore(10f);
        brandPara.setSpacingAfter(10f);

        PdfPCell brandCell = new PdfPCell(brandPara);
        brandCell.setBorder(Rectangle.BOX);
        brandCell.setBorderColor(BaseColor.DARK_GRAY);
        brandCell.setBackgroundColor(headerBgColor);
        brandCell.setPadding(10f);
        brandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        brandCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        headerBox.addCell(brandCell);

        // === Add Header to Document ===
        document.add(headerBox);

        // === Title ===
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD, new BaseColor(0, 70, 127));
        Paragraph title = new Paragraph("COURSE COMPLETION CERTIFICATE", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(20);
        title.setSpacingAfter(20);
        document.add(title);

        // === Certificate No ===
        Font italicSmall = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, BaseColor.BLACK);
        String certNo = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Paragraph certNum = new Paragraph("Certificate No: " + certNo, italicSmall);
        certNum.setAlignment(Element.ALIGN_RIGHT);
        certNum.setSpacingAfter(10);  // spacing below cert number
        document.add(certNum);

        // === Sub-title: TO WHOM IT MAY CONCERN ===
        Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph concernTitle = new Paragraph("TO WHOM IT MAY CONCERN", subTitleFont);
        concernTitle.setAlignment(Element.ALIGN_CENTER);
        concernTitle.setSpacingBefore(10);
        concernTitle.setSpacingAfter(20);
        document.add(concernTitle);

        // === Date Range ===
        Date fromDate1 = student.getAdmissionDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate1);
        cal.add(Calendar.MONTH, 6);
        Date toDate1 = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String from = sdf.format(fromDate1);
        String to = sdf.format(toDate1);

        // === Certificate Body ===
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph body = new Paragraph();
        body.setAlignment(Element.ALIGN_JUSTIFIED);
        body.setSpacingBefore(30);
        body.setSpacingAfter(20);
        body.add(new Phrase("This is to certify that ", textFont));
        body.add(new Phrase(student.getName(), boldFont));
        body.add(new Phrase(" has successfully completed the ", textFont));
        body.add(new Phrase(student.getCourse(), boldFont));
        body.add(new Phrase(" course at ", textFont));
        body.add(new Phrase("FG Infotech", boldFont));
        body.add(new Phrase(" during the period from ", textFont));
        body.add(new Phrase(from, boldFont));
        body.add(new Phrase(" to ", textFont));
        body.add(new Phrase(to + ". ", boldFont));
        body.add(new Phrase("The student has shown commendable performance, technical skills, and consistent dedication during the training.", textFont));
        document.add(body);

        // === Closing Note ===
        Paragraph note = new Paragraph("We congratulate and wish them continued success in their career journey.", textFont);
        note.setAlignment(Element.ALIGN_CENTER);
        note.setSpacingAfter(100);
        document.add(note);

//     // === Signature Section ===
//        PdfPTable signTable = new PdfPTable(2);
//        signTable.setWidthPercentage(100);
//        signTable.setSpacingBefore(10f);
//        signTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//
//        // Common font
//        Font signNameFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
//
//        // === Signature 1 (Left) ===
//        // Image
//        Image sig1 = Image.getInstance(new ClassPathResource("static/images/ChandrakantSSign.png").getURL());
//        sig1.scaleToFit(40, 15);
//        PdfPCell sig1ImageCell = new PdfPCell(sig1, true);
//        sig1ImageCell.setBorder(Rectangle.NO_BORDER);
//        sig1ImageCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//        sig1ImageCell.setFixedHeight(25f); // Ensure same height for image and name
//
//        // Name
//        PdfPCell sig1NameCell = new PdfPCell(new Phrase("Chandrakant Shinde\nDirector, FG Infotech", signNameFont));
//        sig1NameCell.setBorder(Rectangle.NO_BORDER);
//        sig1NameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//        sig1NameCell.setVerticalAlignment(Element.ALIGN_TOP);
//        sig1NameCell.setFixedHeight(25f); // Same height
//
//        // === Signature 2 (Right) ===
//        Image sig2 = Image.getInstance(new ClassPathResource("static/images/ShrikantSSSign.png").getURL());
//        sig2.scaleToFit(40, 15);
//        PdfPCell sig2ImageCell = new PdfPCell(sig2, true);
//        sig2ImageCell.setBorder(Rectangle.NO_BORDER);
//        sig2ImageCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        sig2ImageCell.setFixedHeight(25f);
//
//        PdfPCell sig2NameCell = new PdfPCell(new Phrase("Shrikant Shinde\nTrainer, FG Infotech", signNameFont));
//        sig2NameCell.setBorder(Rectangle.NO_BORDER);
//        sig2NameCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        sig2NameCell.setVerticalAlignment(Element.ALIGN_TOP);
//        sig2NameCell.setFixedHeight(25f);
//
//        // === Nested Tables for Each Side (Image + Name stacked vertically) ===
//        PdfPTable leftSignature = new PdfPTable(1);
//        leftSignature.setWidthPercentage(100);
//        leftSignature.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//        leftSignature.addCell(sig1ImageCell);
//        leftSignature.addCell(sig1NameCell);
//
//        PdfPTable rightSignature = new PdfPTable(1);
//        rightSignature.setWidthPercentage(100);
//        rightSignature.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//        rightSignature.addCell(sig2ImageCell);
//        rightSignature.addCell(sig2NameCell);
//
//        // Wrap nested tables in outer cells
//        PdfPCell leftWrapper = new PdfPCell(leftSignature);
//        leftWrapper.setBorder(Rectangle.NO_BORDER);
//        PdfPCell rightWrapper = new PdfPCell(rightSignature);
//        rightWrapper.setBorder(Rectangle.NO_BORDER);
//
//        // Final row to add both
//        signTable.addCell(leftWrapper);
//        signTable.addCell(rightWrapper);
//
//        // Add to document
//        document.add(signTable);

        // === Signature Section ===
        PdfPTable signTable = new PdfPTable(2);
        signTable.setWidthPercentage(100);
        signTable.setSpacingBefore(10f);
        signTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        // Common font
        Font signNameFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
       
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

          // === Signature 2 (Center) ===
          Image sigMid = Image.getInstance(new ClassPathResource("static/images/HemantSSSign.png").getURL());
          sigMid.scaleToFit(40, 15);
          PdfPCell sigMidImageCell = new PdfPCell(sigMid, true);
          sigMidImageCell.setBorder(Rectangle.NO_BORDER);
          sigMidImageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
          sigMidImageCell.setFixedHeight(25f);

          PdfPCell sigMidNameCell = new PdfPCell(new Phrase("Hemant Kawar\nTrainer, FG Infotech", signNameFont));
          sigMidNameCell.setBorder(Rectangle.NO_BORDER);
          sigMidNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
          sigMidNameCell.setVerticalAlignment(Element.ALIGN_TOP);
          sigMidNameCell.setFixedHeight(25f);

          PdfPTable midSignature = new PdfPTable(1);
          midSignature.setWidthPercentage(100);
          midSignature.getDefaultCell().setBorder(Rectangle.NO_BORDER);
          midSignature.addCell(sigMidImageCell);
          midSignature.addCell(sigMidNameCell);

          // === Signature 3 (Right) ===
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

          // === Wrap All 3 in Outer Table ===
          PdfPTable signTable1 = new PdfPTable(3); // 3 columns for 3 signatures
          signTable1.setWidthPercentage(100);
          signTable1.setSpacingBefore(20f);

          PdfPCell leftWrapper = new PdfPCell(leftSignature);
          leftWrapper.setBorder(Rectangle.NO_BORDER);
          PdfPCell midWrapper = new PdfPCell(midSignature);
          midWrapper.setBorder(Rectangle.NO_BORDER);
          PdfPCell rightWrapper = new PdfPCell(rightSignature);
          rightWrapper.setBorder(Rectangle.NO_BORDER);

          signTable1.addCell(leftWrapper);
          signTable1.addCell(midWrapper);
          signTable1.addCell(rightWrapper);

          // === Add to Document ===
          document.add(signTable1);

     // === Footer Date (Issued On) ===
        Paragraph footerDate = new Paragraph("Issued on: " + new SimpleDateFormat("dd MMM yyyy hh:mm a").format(new Date()),
                new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.DARK_GRAY));
        footerDate.setAlignment(Element.ALIGN_LEFT);
        footerDate.setSpacingBefore(15f); // Increased space above
        footerDate.setSpacingAfter(8f);   // Space before address
        document.add(footerDate);
     

     // === QR Message Text ===
     String qrText = "FG Infotech Certificate Verification ✅\n\n"
             + "Student: " + student.getName() + "\n"
             + "Course: " + student.getCourse() + "\n"
             + "Duration: " + from + " – " + to + "\n"
             + "Certificate No: " + certNo + "\n\n"
             + "✅ This student has successfully completed training at FG Infotech.";

     // === Add Validation Message (above QR)
     Font qrMsgFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.DARK_GRAY);
     Paragraph qrMsg = new Paragraph("Scan to validate certificate", qrMsgFont);
     qrMsg.setAlignment(Element.ALIGN_CENTER);
     qrMsg.setSpacingBefore(10f);
     qrMsg.setSpacingAfter(2f);
     document.add(qrMsg);

     // === QR Code with Validation Message Inside
     try {
         Image qrImage = generateQRCode(qrText, 50, 50);
         qrImage.scaleAbsolute(50, 50);
         qrImage.setAlignment(Image.ALIGN_CENTER);
         qrImage.setSpacingAfter(5f);
         document.add(qrImage);
     } catch (Exception e) {
         e.printStackTrace();
     }

        // === Footer Address Box (BOTTOM-ATTACHED, NO SPACE) ===
        PdfPTable footerBox = new PdfPTable(1);
        footerBox.setTotalWidth(PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
        footerBox.setLockedWidth(true);
        footerBox.setHorizontalAlignment(Element.ALIGN_CENTER); // Full-width

        BaseColor lightBlue = new BaseColor(225, 235, 245); // Real certificate-like color
        Font addressFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

        PdfPCell footerCell = new PdfPCell(new Phrase(
            "Address: FG Infotech, Online IT Training Institute, Pune & Mumbai\nWebsite: https://fginfotech.in | Email: future.growing.infotech@gmail.com",
            addressFont));
        footerCell.setBackgroundColor(lightBlue);
        footerCell.setBorder(Rectangle.TOP); // Optional top border
        footerCell.setBorderColor(BaseColor.GRAY);
        footerCell.setPaddingTop(6f);
        footerCell.setPaddingBottom(6f);
        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerBox.addCell(footerCell);

        // No spacer here (so footer hugs the bottom)
        document.add(footerBox);

        //Finalize the document
        document.close();
        return baos.toByteArray();
    }

    private Image generateQRCode(String qrText, int width, int height) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, width, height);

        // Transparent background config: foreground = black, background = transparent (0x00FFFFFF)
        MatrixToImageConfig config = new MatrixToImageConfig(
                0xFF000000, // Black for QR (foreground)
                0x00FFFFFF  // Transparent for background (ARGB: alpha=0)
        );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", out, config);
        return Image.getInstance(out.toByteArray());
    }

	

}
