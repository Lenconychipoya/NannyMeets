package zw.co.appsareus.nannymeets.models;

public class SearchFilter {
    private int minAge;
    private int maxAge;
    private String gender;
    private String city;
    private String country;
    private String religion;
    private int minSalaryExp;
    private int maxSalaryExp;
    private String typeOfEmployement;

    public SearchFilter(int minAge, int maxAge, String gender, String city, String country, String religion, int minSalaryExp, int maxSalaryExp, String typeOfEmployement) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.city = city;
        this.country = country;
        this.religion = religion;
        this.minSalaryExp = minSalaryExp;
        this.maxSalaryExp = maxSalaryExp;
        this.typeOfEmployement = typeOfEmployement;
    }

    public int getMinAge() {
        return minAge;
    }

    public SearchFilter setMinAge(int minAge) {
        this.minAge = minAge;
        return this;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public SearchFilter setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public SearchFilter setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getCity() {
        return city;
    }

    public SearchFilter setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public SearchFilter setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getReligion() {
        return religion;
    }

    public SearchFilter setReligion(String religion) {
        this.religion = religion;
        return this;
    }

    public int getMinSalaryExp() {
        return minSalaryExp;
    }

    public SearchFilter setMinSalaryExp(int minSalaryExp) {
        this.minSalaryExp = minSalaryExp;
        return this;
    }

    public int getMaxSalaryExp() {
        return maxSalaryExp;
    }

    public SearchFilter setMaxSalaryExp(int maxSalaryExp) {
        this.maxSalaryExp = maxSalaryExp;
        return this;
    }

    public String getTypeOfEmployement() {
        return typeOfEmployement;
    }

    public SearchFilter setTypeOfEmployement(String typeOfEmployement) {
        this.typeOfEmployement = typeOfEmployement;
        return this;
    }
}
