package bk.vinhdo.taxiads.models;

import com.j256.ormlite.table.TableUtils;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import bk.vinhdo.taxiads.TaxiApplication;
import bk.vinhdo.taxiads.database.DatabaseHelper;

/**
 * Created by vinhdo on 4/29/15.
 */
public class BaseModel implements Serializable {
    protected static DatabaseHelper databaseHelper = DatabaseHelper.getInstance(TaxiApplication.getAppContext());
    public static boolean clear(Class<? extends BaseModel> clazz) {
        try {
            if (clazz == UserModel.class) {
                TableUtils.clearTable(databaseHelper.getConnectionSource(), User.class);
            } else if (clazz == AddressModel.class) {
                TableUtils.clearTable(databaseHelper.getConnectionSource(), AddressModel.class);
            } else if (clazz == TimeOpenModel.class) {
                TableUtils.clearTable(databaseHelper.getConnectionSource(), TimeOpenModel.class);
            } else {
                throw new SQLException();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static BaseModel get(Class<? extends BaseModel> clazz, String id) {
        try {
            if (clazz == UserModel.class) {
                return databaseHelper.getUserDao().queryForId(id);
            } else if (clazz == AddressModel.class) {
                return databaseHelper.getAddressDao().queryForId(id);
            } else if (clazz == TimeOpenModel.class) {
                return databaseHelper.getTimeOpenDao().queryForId(id);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<? extends BaseModel> getAll(Class<? extends BaseModel> clazz) {
        try {
            if (clazz == UserModel.class) {
                return databaseHelper.getUserDao().queryForAll();
            } else if (clazz == AddressModel.class) {
                return databaseHelper.getAddressDao().queryForAll();
            } else if (clazz == TimeOpenModel.class) {
                return databaseHelper.getTimeOpenDao().queryForAll();
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create() {
        try {
            if (this instanceof UserModel) {
                databaseHelper.getUserDao().createOrUpdate((UserModel) this);
            } else if (this instanceof AddressModel) {
                databaseHelper.getAddressDao().createOrUpdate((AddressModel) this);
            } else if (this instanceof TimeOpenModel) {
                databaseHelper.getTimeOpenDao().createOrUpdate((TimeOpenModel) this);
            } else {
                throw new SQLException();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update() {
        try {
            if (this instanceof UserModel) {
                UserModel ridesUser = (UserModel) this;
                databaseHelper.getUserDao().update(ridesUser);
            } else if (this instanceof AddressModel) {
                databaseHelper.getAddressDao().update((AddressModel) this);
            } else if (this instanceof TimeOpenModel) {
                databaseHelper.getTimeOpenDao().update((TimeOpenModel) this);
            } else {
                throw new SQLException();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete() {
        try {
            if (this instanceof UserModel) {
                databaseHelper.getUserDao().delete((UserModel) this);
            } else if (this instanceof AddressModel) {
                databaseHelper.getAddressDao().delete((AddressModel) this);
            } else if (this instanceof TimeOpenModel) {
                databaseHelper.getTimeOpenDao().delete((TimeOpenModel) this);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
