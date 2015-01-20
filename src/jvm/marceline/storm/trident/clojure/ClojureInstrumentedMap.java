package marceline.storm.trident.clojure;

import backtype.storm.task.IMetricsContext;
import backtype.storm.utils.Utils;
import marceline.storm.trident.state.map.IInstrumentedMap;
import clojure.lang.IFn;
import clojure.lang.RT;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ClojureInstrumentedMap implements IInstrumentedMap<Object> {
  List<Object> _params;
  List<String> _fnSpec;
  IInstrumentedMap _instrumentedMap;

  public ClojureInstrumentedMap(List fnSpec, List<Object> params) {
    _params = params;
    _fnSpec = fnSpec;

    try {
      IFn hof = Utils.loadClojureFn(_fnSpec.get(0), _fnSpec.get(1));
      _instrumentedMap = (IInstrumentedMap) hof.applyTo(RT.seq(_params));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Object> multiGet(List<List<Object>> keys) {
    return _instrumentedMap.multiGet(keys);
  }

  @Override
  public void multiPut(List<List<Object>> keys, List<Object> vals) {
    _instrumentedMap.multiPut(keys, vals);
  }

  @Override
  public void instrument(Map conf, IMetricsContext metrics) {
    _instrumentedMap.instrument(conf, metrics);
  }
}