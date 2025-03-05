package utils;

import org.assertj.core.api.AbstractAssert;

public abstract class AbstractCustomAssert<SELF extends AbstractAssert<SELF, O>, O>
    extends AbstractAssert<SELF, O> {

  protected AbstractCustomAssert(O o, Class<?> selfType) {
    super(o, selfType);
  }
}
