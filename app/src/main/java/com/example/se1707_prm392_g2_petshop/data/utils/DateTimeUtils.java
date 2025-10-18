package com.example.se1707_prm392_g2_petshop.data.utils;

// THÊM CÁC DÒNG NÀY VÀO:
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;
public class DateTimeUtils {
    // Định nghĩa một formatter cố định để tái sử dụng
    // HH là 24 giờ (00-23), mm là phút
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Trích xuất giờ và phút (định dạng "HH:mm") từ một chuỗi timestamp ISO.
     *
     * @param isoTimestamp Chuỗi thời gian đầu vào (ví dụ: "2025-10-16T13:39:12.5611872")
     * @return Một chuỗi định dạng "HH:mm" (ví dụ: "13:39"),
     * hoặc null (hoặc chuỗi rỗng) nếu parse lỗi.
     */
    public static String getFormattedTime(String isoTimestamp) {
        if (isoTimestamp == null || isoTimestamp.isEmpty()) {
            return null; // Hoặc trả về "" tùy bạn
        }

        try {
            // 1. Parse chuỗi thành đối tượng LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(isoTimestamp);

            // 2. Định dạng đối tượng đó thành chuỗi "HH:mm"
            return dateTime.format(TIME_FORMATTER);

        } catch (DateTimeParseException e) {
            // Xử lý trường hợp chuỗi đầu vào không đúng định dạng
            e.printStackTrace(); // In lỗi ra log
            return null; // Hoặc trả về ""
        }
    }

}
