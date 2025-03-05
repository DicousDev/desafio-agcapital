package utils;

import java.util.Arrays;
import java.util.Collection;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.ValueMatcherException;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

public class CustomComparator extends DefaultComparator {

  private final Collection<Customization> customizations;

  public CustomComparator(JSONCompareMode mode,  Customization... customizations) {
    super(mode);
    this.customizations = Arrays.asList(customizations);
  }

  @Override
  public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) throws JSONException {
    Customization customization = getCustomization(prefix);
    if (customization != null) {
      try {
        if (!customization.matches(prefix, actualValue, expectedValue, result)) {
          result.fail(prefix, expectedValue, actualValue);
        }
      }
      catch (ValueMatcherException e) {
        result.fail(prefix, e);
      }
    } else {
      super.compareValues(prefix, expectedValue, actualValue, result);
    }
  }

  private Customization getCustomization(String path) {
    for (Customization customization : customizations)
      if (customization.appliesToPath(path))
        return customization;
    return null;
  }
}
