package re.restauran_manager.business.service.Impl;

import re.restauran_manager.business.dao.TableDao;
import re.restauran_manager.business.service.IService.ITableService;
import re.restauran_manager.model.enties.Table;
import re.restauran_manager.model.enums.TableStatus;
import re.restauran_manager.utils.ColorConstants;

import java.util.List;

public class ITableServiceImpl implements ITableService {
    private final TableDao tableDao = TableDao.getInstance();
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
        if (item == null || item.getNumber_seats() <= 0) {
            System.out.println(ColorConstants.ERROR + "Số chỗ ngồi phải lớn hơn 0." + ColorConstants.RESET);
            return false;
        }
        return tableDao.insertTable(item);
    }

    @Override
    public boolean deleteTable(int id) {
        if (tableDao.findById(id) == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy bàn ID: [" + id + "]" + ColorConstants.RESET);
            return false;
        }
        return tableDao.deleteById(id);
    }

    @Override
    public boolean updateSeat(int id, int number_seat) {
        if (number_seat <= 0) {
            System.out.println(ColorConstants.ERROR + "Số lượng ghế không hợp lệ." + ColorConstants.RESET);
            return false;
        }
        if (tableDao.findById(id) == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy bàn ID: " + id + ColorConstants.RESET);
            return false;
        }
        return tableDao.updateSeat(id, number_seat);
    }

    @Override
    public boolean updateStatus(int id, int choice) {
        TableStatus status;
        switch (choice) {
            case 1: status = TableStatus.FREE; break;
            case 2: status = TableStatus.OCCUPIED; break;
            case 3: status = TableStatus.RESERVED; break;
            case 4: status = TableStatus.DAMAGED; break;
            default:
                System.out.println(ColorConstants.ERROR + "Lựa chọn không hợp lệ (1-4)." + ColorConstants.RESET);
                return false;
        }

        if (tableDao.findById(id) == null) {
            System.out.println(ColorConstants.ERROR + "Không tìm thấy bàn ID: " + id + ColorConstants.RESET);
            return false;
        }

        return tableDao.updateStatus(id, status);
    }

    @Override
    public void displayAll() {
        List<Table> tables = tableDao.displayAll();
        if (tables == null || tables.isEmpty()) {
            System.out.println(ColorConstants.WARNING + "Danh sách bàn trống." + ColorConstants.RESET);
            return;
        }
        Table.getHeader();
        tables.forEach(Table::displayData);
        Table.getFooter();
    }

    @Override
    public Table findById(int id) {
        return tableDao.findById(id);
    }

    @Override
    public List<Table> findByStatus(TableStatus status) {
        return tableDao.findByStatus(status);
    }
}