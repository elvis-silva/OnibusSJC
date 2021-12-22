package com.nigapps.onibus.sjc.parsers;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import com.nigapps.onibus.sjc.Constants;
import com.nigapps.onibus.sjc.entities.BaseObject;
import com.nigapps.onibus.sjc.entities.BusItem;
import com.nigapps.onibus.sjc.entities.SiteResponse;
import com.nigapps.onibus.sjc.webrequesthandler.IHTMLParser;

public class BlogPostParser implements IHTMLParser {

    private static final String TAG = BlogPostParser.class.getSimpleName();
    private ORDEM ordem = ORDEM.INIT;
    private SiteResponse response;

    enum ORDEM {
        DE_SEGUNDA_A_SEXTA, AOS_SABADOS, AOS_DOMINGOS_E_FERIADOS, AOS_SABADOS_DOMINGOS_E_FERIADOS,
        DE_SEGUNDA_A_DOMINGO_E_FERIADOS, INIT;
    }

    @Override
    public BaseObject parseHTML(String htmlToParse) {
        response = new SiteResponse();
        try {
            Document doc = Jsoup.parse(htmlToParse);
        //    Log.d(TAG, doc.toString());
            response.setPosts(new ArrayList<BusItem>());
            response.setDiasUteisList(new ArrayList<BusItem>());
            response.setSabadosList(new ArrayList<BusItem>());
            response.setDomingosList(new ArrayList<BusItem>());
            response.setSabDomFeriadosList(new ArrayList<BusItem>());
            response.setSegDomFeriadosList(new ArrayList<BusItem>());
            response.setObsList(new ArrayList<BusItem>());
            BusItem post;
            Elements anchors = doc.select(".texto td");
            for (Element anchor : anchors) {
                if (isTextRequired(anchor.text())) {
                    //  Log.d(TAG, "=====> DATA REQUIRED ====> : " + anchor.text());
                    if (anchor.text().equals(Constants.DE_SEGUNDA_A_SEXTA)) {
                        ordem = ORDEM.DE_SEGUNDA_A_SEXTA;
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getDiasUteisList().add(post);
                    } else if (anchor.text().equals(Constants.AOS_SABADOS)) {
                        ordem = ORDEM.AOS_SABADOS;
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getSabadosList().add(post);
                    } else if (anchor.text().equals(Constants.AOS_DOMINGOS_E_FERIADOS)) {
                        ordem = ORDEM.AOS_DOMINGOS_E_FERIADOS;
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getDomingosList().add(post);
                    } else if (anchor.text().equals(Constants.AOS_SABADOS_DOMINGOS_E_FERIADOS)) {
                        ordem = ORDEM.AOS_SABADOS_DOMINGOS_E_FERIADOS;
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getSabDomFeriadosList().add(post);
                    } else if (anchor.text().equals(Constants.DE_SEGUNDA_A_DOMINGO_E_FERIADOS)) {
                        ordem = ORDEM.DE_SEGUNDA_A_DOMINGO_E_FERIADOS;
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getSegDomFeriadosList().add(post);
                    } else {
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getPosts().add(post);
                    }
                 //   Log.d(TAG, "=====> DATA REQUIRED ====> : " + anchor.text());
                } else if (!noTextRequired(anchor.text())) {
                    if(ordem.equals(ORDEM.DE_SEGUNDA_A_SEXTA)) {
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getDiasUteisList().add(post);
                    } else if (ordem.equals(ORDEM.AOS_SABADOS)) {
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getSabadosList().add(post);
                    } else if (ordem.equals(ORDEM.AOS_DOMINGOS_E_FERIADOS)) {
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getDomingosList().add(post);
                    } else if (ordem.equals(ORDEM.AOS_SABADOS_DOMINGOS_E_FERIADOS)) {
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getSabDomFeriadosList().add(post);
                    } else if (ordem.equals(ORDEM.DE_SEGUNDA_A_DOMINGO_E_FERIADOS)) {
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getSegDomFeriadosList().add(post);
                    } else {
                        post = new BusItem();
                        // post.setURL(anchor.attr("href"));
                        post.setText(anchor.text());
                        response.getPosts().add(post);
                    }
                  //  Log.d(TAG, "=====> SAÍDA [" + ordem.toString() +"] =====> : " + anchor.text());
                }

                // Log.d(TAG, "===> DATA NODE SIZE: " + String.valueOf(anchor.));
            }
            anchors = doc.select(".texto p");
            for (Element anchor : anchors) {
                if (anchor.text().contains("(1)")) {
                    post = new BusItem();
                    post.setText(anchor.text().replace("(", "\n("));
                    response.getObsList().add(post);
                    //Log.d(TAG, "=====> TEXTOSM =====> : " + anchor.text());
                }
            }
            Element element = doc.getElementById(
                    "ctl00_ctl00_ctl00_ctl00_ContentPlaceHolderDefault_modelo_master_meio_modelo_duas_colunas_meio_ctl02_horario_itinerario_onibus_layTab_8_lblAtualizadoEm"
            );
            post = new BusItem();
            post.setText(element.text());
        //    Log.d(TAG, "=====> DATA REQUIRED ====> : " + element.text());
            response.getPosts().add(post);
         //   Log.d(TAG, element.text());

           /* response.setPosts(new ArrayList<BusItem>());
            BusItem post;
            Elements anchors = doc.select(".entry-header a");
            for (Element anchor : anchors) {
                post = new BusItem();
                post.setURL(anchor.attr("href"));
                post.setText(anchor.text());
                response.getPosts().add(post);
            }*/
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return response;
    }

    private boolean isTextRequired(String pText) {
        return pText.equals(Constants.DE_SEGUNDA_A_SEXTA) || pText.equals(Constants.AOS_SABADOS) ||
                pText.equals(Constants.AOS_DOMINGOS_E_FERIADOS) || pText.contains(Constants.ATUALIZADO_EM_TEXT) ||
                pText.equals(Constants.HORARIOS) || pText.equals(Constants.ITINERARIO) ||
                pText.equals(Constants.NOME_DA_LINHA) || pText.equals(Constants.NUMERO_DA_LINHA) ||
                pText.equals(Constants.OBSERVACAO) || pText.equals(Constants.SENTIDO) ||
                pText.equals(Constants.AOS_SABADOS_DOMINGOS_E_FERIADOS) ||
                pText.equals(Constants.DE_SEGUNDA_A_DOMINGO_E_FERIADOS);
    }

    private boolean noTextRequired(String pText) {
        return pText.contains("De segunda-feira a sexta-feira   0 às 6h") ||
                pText.contains("Aos sábados   0 às 6h") || pText.contains("Aos domingos e feriados   0 às 6h") ||
                pText.contains("Aos sábados, domingos e feriados   0 às 6h");
    }

    public SiteResponse getResponse() {
        return response;
    }
}