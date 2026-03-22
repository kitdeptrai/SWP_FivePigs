package com.fivepigs.app.web.reviewer;

import com.fivepigs.app.config.Db;
import com.fivepigs.app.model.ReviewGuideline;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

@WebServlet("/reviewer_guideline_update")
public class ReviewerGuidelineUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {

            int guidelineId = Integer.parseInt(req.getParameter("guidelineId"));

            String category = req.getParameter("category");
            String priority = req.getParameter("priority");
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String icon = req.getParameter("icon");
            String color = req.getParameter("color");

            String[] itemTexts = req.getParameterValues("itemText");

            Connection con = Db.getConnection();
            con.setAutoCommit(false);

            // ===== update guideline =====
            String updateGuideline
                    = "UPDATE Review_Guideline "
                    + "SET category=?, priority=?, title=?, description=?, icon=?, color=?, updated_at=NOW() "
                    + "WHERE guideline_id=?";

            PreparedStatement ps = con.prepareStatement(updateGuideline);

            ps.setString(1, category);
            ps.setString(2, priority);
            ps.setString(3, title);
            ps.setString(4, description);
            ps.setString(5, icon);
            ps.setString(6, color);
            ps.setInt(7, guidelineId);

            ps.executeUpdate();

            // ===== delete old items =====
            PreparedStatement psDelete
                    = con.prepareStatement("DELETE FROM Review_Guideline_Item WHERE guideline_id=?");

            psDelete.setInt(1, guidelineId);
            psDelete.executeUpdate();

            // ===== insert new items =====
            if (itemTexts != null) {

                String insertItem
                        = "INSERT INTO Review_Guideline_Item(guideline_id, item_text, sort_order) VALUES(?,?,?)";

                PreparedStatement psItem = con.prepareStatement(insertItem);

                int order = 1;

                for (String text : itemTexts) {

                    if (text == null) {
                        continue;
                    }

                    text = text.trim();

                    if (text.isEmpty()) {
                        continue;
                    }

                    psItem.setInt(1, guidelineId);
                    psItem.setString(2, text);
                    psItem.setInt(3, order++);

                    psItem.addBatch();
                }

                psItem.executeBatch();
            }

            con.commit();

            resp.sendRedirect(req.getContextPath() + "/reviewer_guidelines");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("reviewer_guidelines");
        }
    }
}
