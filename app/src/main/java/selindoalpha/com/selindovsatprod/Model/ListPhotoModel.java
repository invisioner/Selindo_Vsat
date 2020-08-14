package selindoalpha.com.selindovsatprod.Model;

public class ListPhotoModel {

    private String imageurl;
    private String name;

    public ListPhotoModel(String imageurl, String name) {
        this.name = name;
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public String getImageurl() {
        return imageurl;
    }
}
