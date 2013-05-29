package com.mycurrentip.tarefa;

import java.util.HashMap;

import android.os.AsyncTask;

import com.mycurrentip.MyCurrentIP;
import com.mycurrentip.classes.Data;
import com.mycurrentip.classes.Historico;
import com.mycurrentip.util.Constantes;
import com.mycurrentip.util.Enderecos;


public class TarefaAtualizaIP extends AsyncTask<Boolean, String, HashMap<String, String>> {

	private MyCurrentIP myCurrentIP;

	public TarefaAtualizaIP(MyCurrentIP myCurrentIP) {
		this.myCurrentIP = myCurrentIP;
	}

	@Override
	protected HashMap<String, String> doInBackground(Boolean... argv) {
		HashMap<String, String> enderecos = new HashMap<String, String>();
		enderecos.put(Constantes.IP_LOCAL, Enderecos.getEnderecoIP(argv[0]));
		enderecos.put(Constantes.MAC, Enderecos.getEnderecoMAC());
		return enderecos;
	}

	@Override
	protected void onPostExecute(HashMap<String, String> resposta) {
		String ip_local = resposta.get(Constantes.IP_LOCAL);
		myCurrentIP.getCampoTextoIP().setText(ip_local);
		myCurrentIP.getCampoTextoMAC().setText(resposta.get(Constantes.MAC));

		Historico historico = new Historico();
		historico.setIp(ip_local);
		historico.setData_hora(Data.getDataHoraAtual());
		myCurrentIP.getRepoHistorico().insert(historico);
	}
}