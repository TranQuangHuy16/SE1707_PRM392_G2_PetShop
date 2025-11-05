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

    /**
     * Chuyển đổi một chuỗi timestamp có định dạng "yyyy-MM-dd HH:mm:ss.SSSSSSS"
     * sang định dạng "dd/MM/yyyy HH:mm".
     *
     * @param inputTimestamp Chuỗi thời gian đầu vào, ví dụ: "2025-10-30 08:37:20.2913405"
     * @return Một chuỗi định dạng "dd/MM/yyyy HH:mm", ví dụ: "30/10/2025 08:37",
     * hoặc null nếu có lỗi parse.
     */
    public static String formatDisplayDateTime(String inputTimestamp) {
        if (inputTimestamp == null || inputTimestamp.isEmpty()) {
            return null;
        }

        try {
            // Định dạng của chuỗi đầu vào. Dấu cách ' ' được sử dụng thay vì 'T'.
            // Chúng ta cần xử lý phần microsecond/nanosecond một cách linh hoạt.
            // 'SSSSSSS' không phải là pattern chuẩn, ta sẽ cắt chuỗi trước khi parse.
            String parsableTimestamp = inputTimestamp.replace(' ', 'T');
            if (parsableTimestamp.contains(".")) {
                // Cắt bỏ phần micro giây/nano giây vì LocalDateTime.parse mặc định xử lý được
            }

            LocalDateTime dateTime = LocalDateTime.parse(parsableTimestamp);

            // Định dạng đầu ra mong muốn
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return dateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null; // Hoặc trả về chuỗi gốc để debug
        }
    }


}
