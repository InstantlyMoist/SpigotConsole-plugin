package me.kyllian.spigotconsole.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class QRCode {

    private final QRCodeWriter qrCodeWriter;
    private final StringBuilder stringBuilder;

    public QRCode() {
        qrCodeWriter = new QRCodeWriter();
        stringBuilder = new StringBuilder();
    }

    public String generate(String text) {
        try {
            Map<EncodeHintType, Object> encoderHints = new HashMap<>();
            encoderHints.put(EncodeHintType.QR_COMPACT, true);
            final BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 0, 0, encoderHints);

            int height = bitMatrix.getHeight();
            int width = bitMatrix.getWidth();

            stringBuilder.append(System.lineSeparator());
            for (int y = 4; y < height - 4; y++) {
                for (int x = 4; x < width - 4; x++) {
                    char character = bitMatrix.get(x, y) ? '█' : ' ';
                    stringBuilder.append(character).append(character);
                }
                stringBuilder.append(System.lineSeparator());
            }
            return stringBuilder.toString();
        } catch (Exception exception) {
            return "Error creating QR code.";
        }
    }
}
