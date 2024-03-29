package de.atomspace.timeline.tapestry.pages.moment;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.internal.services.CookieSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.ektorp.Attachment;
import org.ektorp.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import de.atomspace.timeline.moment.domain.Moment;
import de.atomspace.timeline.moment.service.MomentService;

public class Upload {

    @Autowired
    @Inject
    MomentService momentService;

    @SessionAttribute
    String visibleKey;

    @Inject
    RequestGlobals _requestGlobals;

    @Inject
    CookieSource cookieSource;

    @Inject
    @Property
    Request request;

    @Inject
    @Property
    Response response;

    @Property
    UploadedFile file;

    @Property
    String description;

    @Property
    Integer year;

    @Property
    String author;

    @Property
    String licence;

    public void onActivate(Integer year) {
        this.year = year;
    }

    public void onSuccess() throws IOException {

        Moment entity = new Moment();

        entity.setAuthor(author);
        entity.getYears().add(year);
        entity.setYear(year);
        entity.setDescription(description);
        entity.setLicence(licence);
        entity.setVisibleKey(this.getVisibleKey());                     //visible key for user session see no published moments 
        entity.setPublished(false);                                     //default no published moments

        BufferedImage originalImage = ImageIO.read(file.getStream());   //load stream to image buffer

        // ORGINAL IMAGE
        byte[] imageInByte = convertToBytes(originalImage);             //transform to byte array
        String fileBase64 = Base64.encodeBytes(imageInByte);            //encode to base64
        String fileName = fileNameConverter(file.getFileName());        //convert a filename onyl with a-z/0-1/./_
        Attachment fileAttachment = new Attachment(fileName, fileBase64, file.getContentType());
        entity.addInlineAttachment(fileAttachment);                     //add attachment to entity

        // PREVIEW IMAGE
        BufferedImage previewImage = resize(originalImage, 300, 300);   //resize original image
        byte[] imageInByte2 = convertToBytes(previewImage);             //transform to byte array
        String fileBase64Preview = Base64.encodeBytes(imageInByte2);    //encode to base64
        Attachment fileAttachmentPreview = new Attachment("preview_" + fileName, fileBase64Preview, file.getContentType()); //create attachment
        entity.addInlineAttachment(fileAttachmentPreview);              //add attachment to entity

        momentService.add(entity);

    }

    /**
     * Resize a image to jpg with max values for width and heigth
     * @param originalImage
     * @param maxWidth
     * @param maxHeigth
     * @return
     */
    private BufferedImage resize(BufferedImage originalImage, int maxWidth, int maxHeigth) {
        int width = 100;
        int heigth = 100;

        Double orgWidth = Double.valueOf(originalImage.getWidth());
        Double orgHeigth = Double.valueOf(originalImage.getHeight());

        Double factorW = Double.valueOf(maxWidth) / orgWidth;
        Double factorH = Double.valueOf(maxHeigth) / orgHeigth;

        if (factorH < factorW) {
            width = (int) (orgWidth * factorH);
            heigth = (int) (orgHeigth * factorH);
        } else {
            width = (int) (orgWidth * factorW);
            heigth = (int) (orgHeigth * factorW);
        }

        BufferedImage previewImage = new BufferedImage(width, heigth, originalImage.getType());
        Graphics2D graphics2d = previewImage.createGraphics();
        graphics2d.drawImage(originalImage, 0, 0, width, heigth, null);
        graphics2d.dispose();
        return previewImage;
    }

    /**
     * Transform a BufferedImage to byte array
     * @param originalImage
     * @return
     * @throws IOException
     */
    private byte[] convertToBytes(BufferedImage originalImage) throws IOException {
        byte[] imageInByte;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", baos);
        baos.flush();
        imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    /**
     * Remove all bad chars from filename 
     * @param input
     * @return
     */
    private String fileNameConverter(String input) {
        StringBuffer output = new StringBuffer();
        input = input.toLowerCase().trim();
        String[] array = input.split("", 0);

        String c;
        for (int i = 1; i < array.length; i++) {
            c = array[i];
            if (c.matches("[a-z.0-9]")) {
                output.append(c);
            } else {
                output.append("_");
            }
        }
        return output.toString();
    }

    /**
     * Access the VisibleKey for webuser in session can preview the moment
     * @return
     */
    public String getVisibleKey() {
        String visibleKeyCookie = null;
        Cookie[] cookies = cookieSource.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("visibleKey")) {
                    visibleKeyCookie = cookie.getValue();
                }
            }
        }
        if (visibleKeyCookie == null) {
            Cookie cookie = new Cookie("visibleKey", UUID.randomUUID().toString());
            cookie.setMaxAge(31104000); // 360days
            _requestGlobals.getHTTPServletResponse().addCookie(cookie);
            visibleKeyCookie = cookie.getValue();
        }
        return visibleKeyCookie;
    }

}
