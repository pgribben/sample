package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * JAXB Value object IntList wraps a List of Integers
 *
 * Created by pgribben on 06/03/2016.
 *
 */
@XmlRootElement
public class IntList {
    @XmlElement
    List<Integer> data;

    public IntList() {}

    public IntList(List<Integer> data) {
        this.data = data;
    }

    public List<Integer> getData() {
        return data;
    }
}
