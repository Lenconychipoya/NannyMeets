package zw.co.appsareus.nannymeets.models;

public class Employee extends User {

    // type of employement ie stay in or part time or stay out.
    private String experience;
    private String qualifications;
    private String passport;
    private String driversLicence;
    private String religion;
    private String church;
    private Float expectedSalary;
    private String occupation;
    private String jobStatus;
    private String maritalStatus;
    private String languages;
    private String profession;

    public Employee() {
    }

    public Employee(String name, String surname, String dob, String gender, String id, String email, String phone, String address, String natID, String password, String accountType, String username, String city, String country, String experience, String qualifications, String passport, String driversLicence, String religion, String church, Float expectedSalary, String occupation, String jobStatus, String maritalStatus, String languages, String profession) {
        super(name, surname, dob, gender, id, email, phone, address, natID, password, accountType, username, city, country);
        this.experience = experience;
        this.qualifications = qualifications;
        this.passport = passport;
        this.driversLicence = driversLicence;
        this.religion = religion;
        this.church = church;
        this.expectedSalary = expectedSalary;
        this.occupation = occupation;
        this.jobStatus = jobStatus;
        this.maritalStatus = maritalStatus;
        this.languages = languages;
        this.profession = profession;
    }

    public String getExperience() {
        return experience;
    }

    public Employee setExperience(String experience) {
        this.experience = experience;
        return this;
    }

    public String getQualifications() {
        return qualifications;
    }

    public Employee setQualifications(String qualifications) {
        this.qualifications = qualifications;
        return this;
    }

    public String getPassport() {
        return passport;
    }

    public Employee setPassport(String passport) {
        this.passport = passport;
        return this;
    }

    public String getDriversLicence() {
        return driversLicence;
    }

    public Employee setDriversLicence(String driversLicence) {
        this.driversLicence = driversLicence;
        return this;
    }

    public String getReligion() {
        return religion;
    }

    public Employee setReligion(String religion) {
        this.religion = religion;
        return this;
    }

    public String getChurch() {
        return church;
    }

    public Employee setChurch(String church) {
        this.church = church;
        return this;
    }

    public Float getExpectedSalary() {
        return expectedSalary;
    }

    public Employee setExpectedSalary(Float expectedSalary) {
        this.expectedSalary = expectedSalary;
        return this;
    }

    public String getOccupation() {
        return occupation;
    }

    public Employee setOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public Employee setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public Employee setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public String getLanguages() {
        return languages;
    }

    public Employee setLanguages(String languages) {
        this.languages = languages;
        return this;
    }

    public String getProfession() {
        return profession;
    }

    public Employee setProfession(String profession) {
        this.profession = profession;
        return this;
    }
}
