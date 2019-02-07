package zw.co.appsareus.nannymeets.interfaces;

import java.util.ArrayList;

import zw.co.appsareus.nannymeets.models.Employee;

public interface OnGetEmployeesListener {
    void onSuccessGetEmployees(ArrayList<Employee> maids);
    void onFailGetEmployees(String error);
}
