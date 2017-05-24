package nl.yoshuan.pricecomparer.datafetcher;

import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.TypeRef;

import java.util.List;

// This class is specific to AH and will only be used by me
// This should be the implementation class, I believe that I will never have to create a subclass for this impl.
public final class AhDataParser {
    
    private final ReadContext readContext;
    
    public AhDataParser(ReadContext readContext) {
        this.readContext = readContext;
    }
    public List<AhProduct> getAhProduct() {
        TypeRef<List<AhProduct>> typeRef = new TypeRef<List<AhProduct>>() {};
        
        List<AhProduct> product = readContext.read("$._embedded.lanes[*]._embedded.items[*]._embedded.product", typeRef);
        setImgSrcs(product);
        
        return product;
    }
    
    private void setImgSrcs(List<AhProduct> products) {
        TypeRef<List<String>> typeRef = new TypeRef<List<String>>() {};
        
        List<String> imgSrcs = readContext.read("$._embedded.lanes[*]._embedded.items[*]._embedded.product.images[2].link.href", typeRef);
        
        int productsLength = products.size();
        for (int i = 0; i < productsLength; i++) {
            products.get(i).setImageSrc(imgSrcs.get(i));
        }
    }
    
}
