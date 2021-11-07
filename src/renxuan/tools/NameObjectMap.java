package renxuan.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import renxuan.entity.Graph;

public class NameObjectMap {//作为一个可以返回多个值的中间类
	private Map<String, Object> map;

	public NameObjectMap() {
		map = new HashMap<String, Object>();
	}

	public void put(String name, Object object) {
		map.put(name, object);
	}

	public Map getMap() {
		return this.map;
	}

	public Object pre(String key) {
		return this.map.get(key);
	}

	@Override
	public String toString() {
		String result = "";
		if (Graph.showPath) {
			List<PathPair> list = (List<PathPair>) map.get("path");

			int[] pre = (int[]) map.get("pre");
			int[] dis = (int[]) map.get("dis");

			for (int i = 0; i < pre.length; i++) {
				result += "pre:" + pre[i] + "  :index:" + i + "\n";
			}
			result += "=============================================\n";
			for (int i = 0; i < pre.length; i++) {
				result += "dis:" + dis[i] + "  :index:" + i + "\n";
			}
			result += "=============================================\n";
		} else {
			return "cost"+(Integer) map.get("cost");
		}

		return result;
	}

}
