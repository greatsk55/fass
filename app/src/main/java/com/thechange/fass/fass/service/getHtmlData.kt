package com.thechange.fass.fass.service

import com.thechange.fass.fass.model.DataFormat
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by user on 2017. 4. 22..
 */


fun ogTag(urlStr:String) : DataFormat{
    val sb = StringBuilder()
    val con = Jsoup.connect(urlStr)

    /* this browseragant thing is important to trick servers into sending us the LARGEST versions of the images */
    //con.userAgent(SyncStateContract.Constants.BROWSER_USER_AGENT);
    val doc = con.get()

    var text = ""
    val metaOgTitle = doc.select("meta[property=og:description]")
    if (metaOgTitle!=null) {
        text = metaOgTitle.attr("content")
    }
    else {
        text = doc.title()
    }

    var imageUrl = ""
    val metaOgImage = doc.select("meta[property=og:image]")
    if (metaOgImage!=null) {
        imageUrl = metaOgImage.attr("content")
    }

    if (imageUrl!=null) {
        sb.append("<img src='")
        sb.append(imageUrl)
        sb.append("' align='left' hspace='12' vspace='12' width='150px'>")
    }

    if (text!=null) {
        sb.append(text)
    }

    val now = System.currentTimeMillis()
    val date = Date(now)
    val CurDateFormat = SimpleDateFormat("yyyy-MM-dd")

    return DataFormat(urlStr, imageUrl, text, CurDateFormat.format(date))

}