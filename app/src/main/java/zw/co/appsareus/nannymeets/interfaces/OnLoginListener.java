package zw.co.appsareus.nannymeets.interfaces;

import zw.co.appsareus.nannymeets.models.Employee;
import zw.co.appsareus.nannymeets.models.Employer;

public interface OnLoginListener {
    void onSuccessfulLoginEmployee(Employee employee);
    void onSuccessfulLoginEmployer(Employer employer);
    void onFailuredLogin(String error);
}
