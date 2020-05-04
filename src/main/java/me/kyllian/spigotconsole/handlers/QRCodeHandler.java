package me.kyllian.spigotconsole.handlers;

import net.glxn.qrgen.QRCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class QRCodeHandler {

    public static BufferedImage generate(String input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            QRCode.from(input).writeTo(baos);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            return ImageIO.read(bais);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //TODO RETURN ERROR IMAGE?
        return null;
    }
}
