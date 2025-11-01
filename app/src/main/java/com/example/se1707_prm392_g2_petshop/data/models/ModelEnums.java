package com.example.se1707_prm392_g2_petshop.data.models;

public class ModelEnums {
    public enum UserRoleEnum {
        Customer(0),
        Admin(1);

        private final int value;

        UserRoleEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
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
