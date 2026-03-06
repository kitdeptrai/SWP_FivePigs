package com.fivepigs.app.dao;
import com.fivepigs.app.model.Category;
import com.fivepigs.app.model.Software;
import com.fivepigs.app.dao.SoftwareDao;
import config.Db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private final String Get_All_Category = "SELECT * FROM fivepigs.category;";

    public List<Category> GETALLCATEGORY() throws SQLException {
        List<Category> list = new ArrayList<>();
        try(Connection c = Db.getConnection();
            PreparedStatement st = c.prepareStatement(Get_All_Category)) {
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                Category ct = new Category();
                ct.setCategoryId(rs.getInt("category_id"));
                ct.setCategoryName(rs.getString("category_name"));
                list.add(ct);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static void main(String[] args) throws SQLException{
        CategoryDao dao = new CategoryDao();
        List<Category> softwares = dao.GETALLCATEGORY();
        System.out.println(softwares);
    }
}
