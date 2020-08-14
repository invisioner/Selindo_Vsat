package selindoalpha.com.selindovsatprod.Model;

import android.graphics.Bitmap;

public class ListInputSpdModel {

    public String id, vid, category, nominal, nameImages;
    public Bitmap image;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getNameImages() {
        return nameImages;
    }

    public void setNameImages(String nameImages) {
        this.nameImages = nameImages;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
