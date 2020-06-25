const express = require('express');
const app = express();
var request = require('request');
const cheerio = require('cheerio');


app.get("/legal", function (req, res) {
  var options = {
    uri: 'http://www.rojadirectatv.tv/legal.php',
    method: 'GET'
  };

  request(options, function (error, response, body) {
    if (!error && response.statusCode == 200) {
      const $ = cheerio.load(body);
      var data_store = [];
      data_store["0"] = {};
      data_store["0"]["text"] = $("p").html()
      res.send(data_store)
    }
  });
});




app.get("/list", function (req, res) {
  var options = {
    uri: 'http://www.rojadirectatv.tv/',
    method: 'GET'
  };

  request(options, function (error, response, body) {
    if (!error && response.statusCode == 200) {
      const $ = cheerio.load(body);
      var results = $("table").children().last()

      var orario = [];
      var nome_partita = [];
      var url = [];
      var icona = [];
      var categoria = [];
      var url_status = [];

      results.each(function (i, result) {
        $(result)
          .find('span')
          .each(function (index, element) {

            if ($(element).attr('class') != 't')
              icona = icona.concat([$(element).attr('class')]);

            if ($(element).text() != '') {
              var getTime = new Date("2019-05-24T" + $(element).text() + ":18.850Z")
              var addTime = new Date(getTime.getTime() + 1000 * 60 * 60 * 7);
              var time = ((addTime.getHours() < 10 ? '0' : '') + addTime.getHours() + ":" + (getTime.getMinutes() < 10 ? '0' : '') + getTime.getMinutes())
              orario = orario.concat(time);
            }



          });
      });

      results.each(function (i, result) {
        $(result)
          .find('td')
          .each(function (index, element) {

            if ($(element).attr('align') == 'left')
              if ($(element).text().includes(':'))
                categoria = categoria.concat([$(element).text().split(":")[0]]);



          });
      });


      results.each(function (i, result) {
        $(result)
          .find('a')
          .each(function (index, element) {
            if ($(element).text() != '') {
              nome_partita = nome_partita.concat([$(element).text()]);
              url = url.concat([$(element).attr('href')]);
              url_status = url_status.concat([$(element).attr('href').replace(".php", "").replace("/", "")]);
            }
          });
      });

      var data_store = [];

      var icon_dict = new Map();
      icon_dict.set('before am', '️️🅰️')
      icon_dict.set('before arg', '🇦🇷')
      icon_dict.set('before box', '🥊')
      icon_dict.set('before br', '🇧🇷')
      icon_dict.set('before cc2017', '🏆')
      icon_dict.set('before oro', '🏆')
      icon_dict.set('before caf', '🏆')
      icon_dict.set('before ch', '🏆')
      icon_dict.set('before ci', '🚲')
      icon_dict.set('before cl', '🇨🇱')
      icon_dict.set('before co', '🇨🇴')
      icon_dict.set('before de', '🇩🇪')
      icon_dict.set('before ec', '🇪🇨')
      icon_dict.set('before en', '🇬🇧')
      icon_dict.set('before es', '🇪🇸')
      icon_dict.set('before f1', '🏁')
      icon_dict.set('before fr', '🇫🇷')
      icon_dict.set('before it', '🇮🇹')
      icon_dict.set('before mlb', '⚾️')
      icon_dict.set('before mx', '🇲🇽')
      icon_dict.set('before ca2019', '🏆')
      icon_dict.set('before nba', '🏀')
      icon_dict.set('before nl', '🇳🇱')
      icon_dict.set('before pe', '🇵🇪')
      icon_dict.set('before csuda', '🏆')
      icon_dict.set('before tr', '🇹🇷')
      icon_dict.set('before us', '🇺🇸')
      icon_dict.set('before el', '🏆')
      icon_dict.set('before uy', '🇺🇾')
      icon_dict.set('before wwe', '🤼‍')
      icon_dict.set('before suda', '🏆')
      icon_dict.set('before bkb', '🏀')
      icon_dict.set('before tenis', '🎾')
      icon_dict.set('before motogp', '🏍️')
      icon_dict.set('before soccer', '⚽')
      icon_dict.set('before icc', '🏆')



      var url_dict = new Map();
      url_dict.set('bein', '1')
      url_dict.set('beinn', '1')
      url_dict.set('canal-31', '1')
      url_dict.set('canal1', '1')
      url_dict.set('canal2', '1')
      url_dict.set('directv', '1')
      url_dict.set('dplus', '1')
      url_dict.set('espn3', '1')
      url_dict.set('fox2', '1')
      url_dict.set('futbol', '1')
      url_dict.set('gol', '1')
      url_dict.set('santander', '1')
      url_dict.set('skip', '1')
      url_dict.set('canal-42', '1')
      url_dict.set('canal-30', '1')
      url_dict.set('canal-41', '1')




      for (var j = 0; j < nome_partita.length; j++) {

        data_store[j] = {};

        data_store[j]["name"] = url[j]

        data_store[j]["name"] = nome_partita[j]

        if (orario[j] != undefined)
          data_store[j]["time"] = orario[j];
        else
          data_store[j]["time"] = "24/7"


        if (url[j].includes("http") == true) {
          data_store[j]["url"] = url[j];
          console.log(j)
        } else
          data_store[j]["url"] = "http://www.rojadirectatv.tv" + url[j];


        if (url_dict.has(url_status[j]))
          data_store[j]["url_status"] = url_dict.get(url_status[j])
        else
          data_store[j]["url_status"] = "0"


        if (icon_dict.has(icona[j]))
          data_store[j]["icon"] = icon_dict.get(icona[j])
        else
          data_store[j]["icon"] = icona[j].replace(" ", "")


        if (categoria[j] != undefined)
          data_store[j]["class"] = categoria[j];
        else
          data_store[j]["class"] = ""


      }



    }


    res.send(data_store)


  });
});

exports = module.exports = app;
const server = app.listen(process.env.PORT || 1331, function () {});