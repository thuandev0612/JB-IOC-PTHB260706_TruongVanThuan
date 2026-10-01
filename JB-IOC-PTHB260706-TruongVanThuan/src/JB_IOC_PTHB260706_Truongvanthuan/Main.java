package JB_IOC_PTHB260706_Truongvanthuan;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final TicketBookingDAO dao = new TicketBookingDAO();

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static void main(String[] args) {
        int choice;

        do {
            showMenu();
            choice = inputInt("Nhap lua chon: ");

            switch (choice) {
                case 1:
                    showAll();
                    break;

                case 2:
                    addBooking();
                    break;

                case 3:
                    updateBooking();
                    break;

                case 4:
                    deleteBooking();
                    break;

                case 5:
                    searchCustomer();
                    break;

                case 6:
                    searchMovie();
                    break;

                case 7:
                    System.out.println("Thoat chuong trinh!");
                    break;

                default:
                    System.out.println("Lua chon khong hop le!");
            }

        } while (choice != 7);

        sc.close();
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("======================================================");
        System.out.println("          CINEMA TICKET BOOKING MANAGEMENT");
        System.out.println("======================================================");
        System.out.println("1. Danh sach tat ca phieu dat ve");
        System.out.println("2. Them moi phieu dat ve");
        System.out.println("3. Cap nhat thong tin phieu dat ve");
        System.out.println("4. Xoa phieu dat ve");
        System.out.println("5. Tim kiem phieu dat ve theo ten khach hang");
        System.out.println("6. Tim kiem phieu dat ve theo ten phim");
        System.out.println("7. Thoat");
        System.out.println("======================================================");
    }

    private static void showAll() {
        displayBookings(dao.getAllBookings());
    }

    private static void addBooking() {

        System.out.println("\n===== THEM PHIEU DAT VE =====");

        String movieTitle = inputString("Ten phim: ");
        String customerName = inputString("Ten khach hang: ");

        LocalDateTime showTime =
                inputDateTime("Thoi gian chieu (dd/MM/yyyy HH:mm): ");

        int seatQuantity = inputPositiveInt("So luong ghe: ");

        String status = inputStatus();

        TicketBooking booking = new TicketBooking(
                0,
                movieTitle,
                customerName,
                showTime,
                LocalDateTime.now(),
                seatQuantity,
                status
        );

        if (dao.addBooking(booking)) {
            System.out.println("Them thanh cong!");
        }
    }

    private static void updateBooking() {

        System.out.println("\n===== CAP NHAT PHIEU DAT VE =====");

        int bookingId = inputPositiveInt("Nhap booking ID: ");

        String movieTitle = inputString("Ten phim moi: ");
        String customerName = inputString("Ten khach hang moi: ");

        LocalDateTime showTime =
                inputDateTime("Thoi gian chieu moi (dd/MM/yyyy HH:mm): ");

        int seatQuantity = inputPositiveInt("So luong ghe moi: ");

        String status = inputStatus();

        TicketBooking booking = new TicketBooking(
                bookingId,
                movieTitle,
                customerName,
                showTime,
                LocalDateTime.now(),
                seatQuantity,
                status
        );

        if (dao.updateBooking(booking)) {
            System.out.println("Cap nhat thanh cong!");
        }
    }

    private static void deleteBooking() {

        System.out.println("\n===== XOA PHIEU DAT VE =====");

        int bookingId = inputPositiveInt("Nhap booking ID: ");

        System.out.print("Ban co chac muon xoa? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {

            if (dao.deleteBooking(bookingId)) {
                System.out.println("Xoa thanh cong!");
            }

        } else {
            System.out.println("Da huy xoa.");
        }
    }

    private static void searchCustomer() {

        String keyword =
                inputString("Nhap ten khach hang can tim: ");

        displayBookings(
                dao.searchByCustomer(keyword)
        );
    }

    private static void searchMovie() {

        String keyword =
                inputString("Nhap ten phim can tim: ");

        displayBookings(
                dao.searchByMovie(keyword)
        );
    }

    private static void displayBookings(List<TicketBooking> list) {

        if (list.isEmpty()) {
            System.out.println("Khong co du lieu.");
            return;
        }

        System.out.println(
                "----------------------------------------------------------------------------------------------------------------"
        );

        System.out.printf(
                "%-5s %-25s %-20s %-20s %-20s %-8s %-12s%n",
                "ID",
                "Movie",
                "Customer",
                "Show Time",
                "Booking Date",
                "Seats",
                "Status"
        );

        System.out.println(
                "----------------------------------------------------------------------------------------------------------------"
        );

        for (TicketBooking booking : list) {

            System.out.printf(
                    "%-5d %-25s %-20s %-20s %-20s %-8d %-12s%n",
                    booking.getBookingId(),
                    booking.getMovieTitle(),
                    booking.getCustomerName(),
                    booking.getShowTime().format(FORMATTER),
                    booking.getBookingDate().format(FORMATTER),
                    booking.getSeatQuantity(),
                    booking.getStatus()
            );
        }
    }

    private static String inputString(String message) {

        while (true) {

            System.out.print(message);

            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Khong duoc de trong!");
        }
    }

    private static int inputInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        sc.nextLine()
                );

            } catch (NumberFormatException e) {

                System.out.println("Phai nhap so!");
            }
        }
    }

    private static int inputPositiveInt(String message) {

        while (true) {

            int value = inputInt(message);

            if (value > 0) {
                return value;
            }

            System.out.println("Gia tri phai > 0!");
        }
    }

    private static LocalDateTime inputDateTime(String message) {

        while (true) {
            try {
                System.out.print(message);

                return LocalDateTime.parse(
                        sc.nextLine(),
                        FORMATTER
                );

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Sai dinh dang! VD: 10/10/2026 19:30"
                );
            }
        }
    }

    private static String inputStatus() {

        while (true) {

            System.out.println("1. Booked");
            System.out.println("2. Cancelled");
            System.out.println("3. CheckedIn");

            int choice = inputInt("Chon trang thai: ");

            switch (choice) {
                case 1:
                    return "Booked";

                case 2:
                    return "Cancelled";

                case 3:
                    return "CheckedIn";

                default:
                    System.out.println("Khong hop le!");
            }
        }
    }
}
