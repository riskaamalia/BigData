package Jena;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1224A on 6/9/2016.
 */
public class RuleTypeRDF {

    public List<String> Domaintype () {
        List<String> thetypedomain = new ArrayList<>();
        //include all list of domain type and its subclasses
        thetypedomain.add("person");
        /*thetypedomain.add("place");
        thetypedomain.add("work");
        thetypedomain.add("species");
        thetypedomain.add("organisation");*/

        return thetypedomain;
    }

    public String moveToDomainType ( String tipe) throws FileNotFoundException {
        //move the parameter to domain type
        String domaintype="";
        RuleTypeRDF objrule = new RuleTypeRDF();
        List<String> thedomain = new ArrayList<>();
        thedomain.addAll(objrule.Domaintype());
        //buka file, apabila contain ganti deh ke contain itu
        String path = "";
        for (String Domain :objrule.Domaintype()) {
            path = "D:/mediatrac/DATA/"+Domain+"bawahan";
            BufferedReader br = new BufferedReader(new FileReader(path));
            try {
                String line = br.readLine();
                if (tipe.equals(line)) {
                    domaintype = Domain;
                    return domaintype;
                }
            } catch (IOException op) {
                op.getMessage();
            }
        }
        return domaintype;
    }

    public List<String> propertytype () {
        List<String> thetypeproperty = new ArrayList<>();
        //list all the type that is a property
        //ini tampung aja di teks

        return  thetypeproperty;
    }

    public String moveToPropertyType ( String tipe) {
        String Propertytype="";

        return Propertytype;
    }
}
