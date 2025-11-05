package com.example.se1707_prm392_g2_petshop.data.models;

public class ModelEnums {
    public enum UserRoleEnum {
        Customer("Customer"),
        Admin("Admin");

        private final String value;

        UserRoleEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        /** 🔹 Chuyển từ chuỗi ("Admin") sang Enum */
        public static UserRoleEnum fromString(String value) {
            for (UserRoleEnum role : values()) {
                if (role.value.equalsIgnoreCase(value)) return role;
            }
            return Customer; // default nếu không khớp
        }

        /** 🔹 Lấy danh sách tất cả giá trị chuỗi ("Customer", "Admin") */
        public static String[] getValues() {
            UserRoleEnum[] roles = values();
            String[] values = new String[roles.length];
            for (int i = 0; i < roles.length; i++) {
                values[i] = roles[i].getValue();
            }
            return values;
        }

        /**
         * Nếu bạn dùng int mapping (0 = Customer, 1 = Admin)
         * thì cần chuyển từ int sang Enum
         * */
        public static UserRoleEnum fromInt(int value) {
            switch (value) {
                case 1:
                    return Admin;
                case 0:
                default:
                    return Customer;
            }
        }

        /** 🔹 Nếu bạn cần int để lưu trong DB hoặc API */
        public int toInt() {
            switch (this) {
                case Admin:
                    return 1;
                case Customer:
                default:
                    return 0;
            }
        }
    }

    public enum OrderStatusEnum {
        Pending(0),
        Paid(1),
        Cancelled(2);

        private final int value;

        OrderStatusEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum PaymentMethodEnum {
        Cash(0),
        CreditCard(1),
        Momo(2),
        ZaloPay(3);

        private final int value;

        PaymentMethodEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum PaymentStatusEnum {
        Success(0),
        Failed(1),
        Pending(2);

        private final int value;

        PaymentStatusEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}