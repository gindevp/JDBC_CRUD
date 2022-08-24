# JDBC_CRUD

Tạo connection:

private String jdbcURL = "jdbc:mysql://localhost:3306/database";
    private String jdbcUsername = "root";
    private String jdbcPassword = "123456";
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            System.out.println("ket noi thanh cong");
        } catch (SQLException e) {
            System.out.println("Ket noi that bai");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

DAO cơ bản:
public interface DAO <E,K>{
    void insert(E e);
    E select(K k);
    List<E> selectAll();
    boolean delete(K k) ;
    boolean update(E e);
}
  
 CRUD:
    
  Read:
  public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = new jdbcConnect().getConnection(); 
  PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_USERS)) {      // tự động đóng (closeable, try with resource)
 // connection để tạo kết nối, pstmt để nhận câu lệnh truy vấn sql
            ResultSet rs = pstmt.executeQuery();    // result hứng dữ liệu truy xuất bởi execute ( Query: để lấy, Update: để sửa)
            while (rs.next()) {
                int id = rs.getInt("id");
   ...
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }
  
  Create:
  public void insertUser(User user) throws SQLException {
        try (Connection connection = new jdbcConnect().getConnection(); 
  PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getName());
..
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
  
  Update:
  public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try(Connection connection= new jdbcConnect().getConnection(); 
  PreparedStatement preparedStatement= connection.prepareStatement(UPDATE_USERS_SQL)){
            preparedStatement.setString(1,user.getName());
...
            rowUpdated=preparedStatement.executeUpdate()>0;
        }
        return rowUpdated;
    }
  
  Delete:
   public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = new jdbcConnect().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL)) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
  
