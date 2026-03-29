package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.dao.TableDao;
import re.restauran_manager.business.service.IService.ITableService;
import re.restauran_manager.model.enties.MenuItems;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.model.enums.TableStatus;
import re.restauran_manager.utils.ColorConstants;

import java.util.List;

public class ITableServiceImpl implements ITableService {
    private TableDao tableDao = TableDao.getInstance();
    private static ITableServiceImpl instance;

    private ITableServiceImpl() {}

    public static ITableServiceImpl getInstance() {
        if (instance == null) {
            instance = new ITableServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean add(Table item) {
        if (item == null || item.getNumber_seats() < 0 ) {
            System.out.println(ColorConstants.ERROR + "Dữ liệu không hợp lệ." + ColorConstants.RESET);
            return false;
        }
        return tableDao.insertTable(item);
    }

    @Override
    public boolean deleteTable(int id) {
       Table table = tableDao.findById(id);

        if (table == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy bàn có ID : [" + id + "]" + ColorConstants.RESET);
            return false;
        }
        return tableDao.deleteById(table.getTable_id());
    }

    @Override
    public boolean updateSeat(int id, int number_seat) {
        if (number_seat < 0) {
            System.out.println(ColorConstants.ERROR + "Số lượng ghế không được âm." + ColorConstants.RESET);
            return false;
        }
        if (tableDao.findById(id) == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy bàn." + ColorConstants.RESET);
            return false;
        }
        return tableDao.updateSeat(id, number_seat);
    }

    @Override
    public boolean updateStatus(int id, int choice) {
        if (id <= 0) {
            System.out.println(ColorConstants.ERROR + "ID không hợp lệ" + ColorConstants.RESET);
            return false;
        }

        if (tableDao.findById(id) == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy bàn ID: " + id + ColorConstants.RESET);
            return false;
        }

        TableStatus status;
        if (choice == 1) {
            status = TableStatus.EMPTY;
        } else if (choice == 2) {
            status = TableStatus.OCCUPIED;
        } else {
            System.out.println(ColorConstants.ERROR + "Lựa chọn trạng thái không hợp lệ (Chỉ chọn 1 hoặc 2)." + ColorConstants.RESET);
            return false;
        }

        return tableDao.updateStatus(id, status);
    }

    @Override
    public void displayAll() {
        List<Table> tables = tableDao.displayAll();
        if (tables == null || tables.isEmpty()) {
            System.out.println(ColorConstants.ERROR + "Danh sách trống." + ColorConstants.RESET);
            return;
        }

        System.out.println("\n" + ColorConstants.SUCCESS + " DANH SÁCH BÀN " + ColorConstants.RESET);
        Table.getHeader();
        for (Table table : tables) {
            table.displayData();
        }
        Table.getFooter();
    }

    @Override
    public Table findById(int id) {
        if (id <= 0) return null;
        return tableDao.findById(id);
    }
}
