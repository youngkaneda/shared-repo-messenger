package ag.messenger.infra;


import java.util.Date;

import org.json.JSONObject;

import ag.messenger.model.Message;

public class TranslateImpl implements Translate {

	@Override
	public String toJSON(Message m) {
		JSONObject json = new JSONObject();
		json.put("id", m.getId());
		json.put("from", m.getFrom());
		json.put("text", m.getText());
		json.put("date", m.getDate().getTime());
		return json.toString();
	}

	@Override
	public Message fromJSON(String line) {
		JSONObject json = new JSONObject(line);
		Message m = new Message();
		m.setId(json.getInt("id"));
		m.setFrom(json.getString("from"));
		m.setText(json.getString("text"));
		m.setDate(new Date(json.getLong("date")));
		return m;
	}

}
