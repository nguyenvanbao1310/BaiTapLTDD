package vn.iotstar.viewflipper.model;

public class ImageModel {
    private String imageUrl;
    private int imageResId; // Thêm biến này nếu cần dùng ảnh từ res

    // Constructor dành cho ảnh từ URL
    public ImageModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Constructor dành cho ảnh từ resource
    public ImageModel(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getImageResId() {
        return imageResId;
    }
}
