package ap.restaurant.restaurant.services;

import ap.restaurant.restaurant.dao.MenuItemDAO;
import ap.restaurant.restaurant.models.MenuItem;
import java.sql.SQLException;
import java.util.List;

public class MenuService {
    public static List<MenuItem> getFullMenu() throws SQLException {
        return MenuItemDAO.getAllMenuItems();
    }

    public static MenuItem getMenuById(int id) throws SQLException {
        return MenuItemDAO.getMenuItemById(id);
    }
}