package JB_IOC_PTHB260706_Truongvanthuan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketBookingDAO {
    private TicketBooking mapResultSet(ResultSet rs) throws SQLException {
        TicketBooking booking = new TicketBooking();

        booking.setBookingId(rs.getInt("booking_id"));
        booking.setMovieTitle(rs.getString("movie_title"));
        booking.setCustomerName(rs.getString("customer_name"));
        booking.setShowTime(rs.getTimestamp("show_time").toLocalDateTime());
        booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
        booking.setSeatQuantity(rs.getInt("seat_quantity"));
        booking.setStatus(rs.getString("status"));

        return booking;
    }

    public List<TicketBooking> getAllBookings() {
        List<TicketBooking> list = new ArrayList<>();

        String sql = "SELECT * FROM fn_get_all_bookings()";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
        }

        return list;
    }

    public boolean addBooking(TicketBooking booking) {
        String sql = "CALL public.sp_add_booking(?, ?, ?, ?, ?, ?)";;

        try (
                Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall(sql)
        ) {
            cs.setString(1, booking.getMovieTitle());
            cs.setString(2, booking.getCustomerName());
            cs.setTimestamp(3, Timestamp.valueOf(booking.getShowTime()));
            cs.setTimestamp(4, Timestamp.valueOf(booking.getBookingDate()));
            cs.setInt(5, booking.getSeatQuantity());
            cs.setString(6, booking.getStatus());

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Loi them: " + e.getMessage());
            return false;
        }
    }

    public boolean updateBooking(TicketBooking booking) {
        String sql = "CALL sp_update_booking(?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall(sql)
        ) {
            cs.setInt(1, booking.getBookingId());
            cs.setString(2, booking.getMovieTitle());
            cs.setString(3, booking.getCustomerName());
            cs.setTimestamp(4, Timestamp.valueOf(booking.getShowTime()));
            cs.setTimestamp(5, Timestamp.valueOf(booking.getBookingDate()));
            cs.setInt(6, booking.getSeatQuantity());
            cs.setString(7, booking.getStatus());

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Loi cap nhat: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBooking(int bookingId) {
        String sql = "CALL sp_delete_booking(?)";

        try (
                Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall(sql)
        ) {
            cs.setInt(1, bookingId);
            cs.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Loi xoa: " + e.getMessage());
            return false;
        }
    }

    public List<TicketBooking> searchByCustomer(String keyword) {
        List<TicketBooking> list = new ArrayList<>();

        String sql = "SELECT * FROM fn_search_by_customer(?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim kiem: " + e.getMessage());
        }

        return list;
    }

    public List<TicketBooking> searchByMovie(String keyword) {
        List<TicketBooking> list = new ArrayList<>();

        String sql = "SELECT * FROM fn_search_by_movie(?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Loi tim kiem: " + e.getMessage());
        }

        return list;
    }

}
