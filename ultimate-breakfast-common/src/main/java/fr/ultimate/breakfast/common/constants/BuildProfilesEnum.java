package fr.ultimate.breakfast.common.constants;

public enum BuildProfilesEnum
{
    EMBEDDED("embedded"), PROD("prod");

    private String value;

    private BuildProfilesEnum(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
