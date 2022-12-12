public class Util {

    public float bytesConvert(long bytesRecebidos){
        float tamanho_kb = bytesRecebidos /1024f;
        float tamanho_mb = tamanho_kb / 1024f;
        return  tamanho_mb * 8;
    }
}
