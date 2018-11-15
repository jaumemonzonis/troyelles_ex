package net.daw.service;

import java.util.ArrayList;
import net.daw.bean.ProductoBean;
import net.daw.bean.UsuarioBean;

public class RellenarService {

    public ArrayList<ProductoBean> RellenarProducto(int numero) {
        String[] codigo = {"56TT","GTE4","K8J6","JKK1","EEP9","TTP9","YHG5","NH6Y","9IOK","LLIU"};
        String[] desc = { "Util", "utensilio", "aparejo", "instrumento", "aparato", "artefacto", "material",
				"trebejo", "mecanismo", "chirimbolo" };
        String[] desc2 = {"para cortar","para romper","para unir","para copiar","para untar","para colar","para diseñar","para corrosionar","para digitalizar","para mezclar"};
        String foto = "Foto";
        int[] id_tipoProducto = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] existencias = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        
        int maxDatos = 10;
        
        ArrayList<ProductoBean> resultado = new ArrayList<>();
        ProductoBean resultadoProducto;
        for (int i  = 0;i < numero; i++ ){
            resultadoProducto = new ProductoBean();
            resultadoProducto.setCodigo(codigo[randomMath(maxDatos)]);
            resultadoProducto.setDesc(desc[randomMath(maxDatos)] + " " + desc2[randomMath(maxDatos)]);
            resultadoProducto.setPrecio((float) (((int)(Math.random()*10000))* 0.01));
            resultadoProducto.setFoto(foto);
            //resultadoProducto.setId_tipoProducto(id_tipoProducto[randomMath(maxDatos)]);
            resultadoProducto.setExistencias(existencias[randomMath(maxDatos)]);
            resultado.add(resultadoProducto);
        }
        return resultado;
    }
    
    public ArrayList<UsuarioBean> RellenarUsuario(int numero) {
        String[] dni = {"48885521N","45785521O","47895521P","45785511T","45435521N","45786621E","42285521F","76585521N","33385521B","44485521N"};
        String[] nombre = { "Juan", "Maria", "Jose", "Luis", "Amparo", "Sara", "Lara",
				"Pepe", "Rafael", "Alberto" };
        String[] ape1 = {"Rodriguez","Cortes","Mola","Sanz","Lopez","Librada","Pensal","Sansin","Zubiri","Nastal"};
        String[] ape2 = {"Rodriguez","Cortes","Mola","Sanz","Lopez","Librada","Pensal","Sansin","Zubiri","Nastal"};
        String[] login = {"edrs","golo","esww","sall","tgvf","fffff","rest","cany","pill","edsww"}; 
        String[] pass = {"sssss","gfhffg","dfgdf","tgfff","deded","sasaa","eeerr","para","igitaliz","mezclar"};
       
        int[] id_tipoUsuario = {1, 2};
        int maxDatos = 10;
        int maxTipoUsuario=2;
        
        ArrayList<UsuarioBean> resultado = new ArrayList<>();
        UsuarioBean resultadoUsuario;
        
        for (int i  = 0;i < numero; i++ ){
        	resultadoUsuario = new UsuarioBean();
        	resultadoUsuario.setDni(dni[randomMath(maxDatos)]);
        	resultadoUsuario.setNombre(nombre[randomMath(maxDatos)]);
        	resultadoUsuario.setApe1(ape1[randomMath(maxDatos)]);
        	resultadoUsuario.setApe2(ape2[randomMath(maxDatos)]);
        	resultadoUsuario.setLogin(login[randomMath(maxDatos)]);
        	resultadoUsuario.setPass(pass[randomMath(maxDatos)]);
        	resultadoUsuario.setId_tipoUsuario(id_tipoUsuario[randomMath(maxTipoUsuario)]);
            resultado.add(resultadoUsuario);
        }
        return resultado;
    }
    
    private int randomMath(int number){
        return (int) (Math.random()*number);
    }

}
