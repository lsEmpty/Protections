package protections.Utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&', messageHexColor(message));
    }

    private static String messageHexColor(String message) {
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            // Convierte el código HEX al formato §x§R§R§G§G§B§B
            String replacement = "§x§" + hex.charAt(0) + "§" + hex.charAt(1) + "§" +
                    hex.charAt(2) + "§" + hex.charAt(3) + "§" + hex.charAt(4) + "§" + hex.charAt(5);
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

}
