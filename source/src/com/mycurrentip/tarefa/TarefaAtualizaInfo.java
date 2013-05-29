package com.mycurrentip.tarefa;

import java.util.HashMap;

import android.os.AsyncTask;
import android.util.Log;

import com.mycurrentip.MyCurrentIP;
import com.mycurrentip.net.ClienteHttp;
import com.mycurrentip.net.Conexao;
import com.mycurrentip.util.Constantes;
import com.mycurrentip.util.Enderecos;
import com.mycurrentip.util.Json.IpExterno;


public class TarefaAtualizaInfo extends AsyncTask<Boolean, String, HashMap<String, String>> {

	private MyCurrentIP myCurrentIP;
	private ClienteHttp clienteHttp;
	private IpExterno ipExterno;

	public TarefaAtualizaInfo(MyCurrentIP myCurrentIP) {
		this.myCurrentIP = myCurrentIP;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		myCurrentIP.comecouExecucao();
		super.onPreExecute();
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		myCurrentIP.mostrarMensagem(values[0]);
		super.onProgressUpdate(values);
	}
	
	@Override
	protected HashMap<String, String> doInBackground(Boolean... argv) {
		int codResposta = 0;
		int executeCount = 0;
		String ip_interno, mac;
		String ip_externo = "Sem Conexao"; 
		
		publishProgress("Loading...");
		HashMap<String, String> enderecos = new HashMap<String, String>();
		ip_interno = Enderecos.getEnderecoIP(argv[0]);
		enderecos.put(Constantes.IP_LOCAL, ip_interno);
		
		publishProgress("Ip local: " + ip_interno + "\nLoading...");
		mac = Enderecos.getEnderecoMAC();
		enderecos.put(Constantes.MAC, mac);
		publishProgress("Ip local: " + ip_interno + "\nMac: " + mac + "\nVerificando Conexao...");
		
		
		if (Conexao.verificaConexao(myCurrentIP)){
			publishProgress("Ip local: " + ip_interno + "\nMac: " + mac + "\nConexao OK\nLoading...");
			clienteHttp = new ClienteHttp(Constantes.URL_JSON_IP_EXTERNO, "GET");
			do {
				executeCount++;
				clienteHttp.executar();
				codResposta = clienteHttp.getStatus();
				Log.d("codresposta", String.valueOf(codResposta));
			} while (executeCount < 5 && codResposta == 408);
	
			if (codResposta == 200) {
				ipExterno = (IpExterno)clienteHttp.obterJson(IpExterno.class);
				ip_externo = ipExterno.getIp();
				publishProgress("Ip local: " + ip_interno + "\nMac: " + mac + "Ip Externo: " + ip_externo);
			}
		}
		enderecos.put(Constantes.IP_EXTERNO, ip_externo);
		return enderecos;
	}

	@Override
	protected void onPostExecute(HashMap<String, String> resposta) {
		myCurrentIP.terminouExecucao(resposta);
	}
}