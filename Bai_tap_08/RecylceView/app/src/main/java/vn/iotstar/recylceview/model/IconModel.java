package vn.iotstar.recylceview.model;

public class IconModel {
    private int imageResId;
    private String iconName;

    public IconModel(int imageResId, String iconName) {
        this.imageResId = imageResId;
        this.iconName = iconName;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
