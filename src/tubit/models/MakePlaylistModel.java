/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubit.models;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;

/**
 *
 * @author Ofir
 */
public class MakePlaylistModel {
    public ByteArrayInputStream getBias(String path) {
        ByteArrayOutputStream baos = null;
        try {
            BufferedImage bi = ImageIO.read(new File(getClass().getResource(path).toURI()));
            baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
        } catch (IOException ex) {
        } catch (URISyntaxException ex) {
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

}
