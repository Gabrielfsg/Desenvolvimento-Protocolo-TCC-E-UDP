public class Util {

    public float bytesConvert(long bytesRecebidos){
        float tamanho_kb = bytesRecebidos /1024;
        float tamanho_mb = tamanho_kb / 1024;
        return  tamanho_mb;
    }
}
