package re.restauran_manager.business.service.IService;

import re.restauran_manager.model.enties.Table;
import re.restauran_manager.model.enums.TableStatus;

import java.util.List;

public interface ITableService {
    boolean add(Table item);
    boolean deleteTable(int id);
    boolean updateSeat(int id, int number_seat);
    boolean updateStatus(int id, int choice);

    void displayAll();
    Table findById(int id);
    List<Table> findByStatus(TableStatus status);
}
