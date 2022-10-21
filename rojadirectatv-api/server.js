const url_dict = require("./export").url_dict;
const icon_dict = require("./export").icon_dict;
const express = require("express");
const app = express();
const cheerio = require("cheerio");
var axios = require('axios');

const PORT = 1341;
console.log("port: " + PORT);

var timezone = 4;
var siteurl = "https://www.rojadirectatvhd.tv";

app.get("/legal", function (req, res) {

  var config = {
    method: 'get',
    url: siteurl + "/legal.php",
    headers: {}
  };



  axios(config)
    .then(function (response, error) {
      if (!error && response.status == 200) {

        const $ = cheerio.load(response.data);
        var get = $('div[class="texto2"]').contents().text();
        var data_store = [];
        data_store["0"] = {};
        data_store["0"]["text"] = get;
        res.send(data_store);
      }
    })
    .catch(function (error) {
      console.log(error);
    });
});

app.get("/list", function (req, res) {
  var config = {
    method: 'get',
    url: siteurl,
    headers: {}
  };

  axios(config)
    .then(function (response, error) {
      if (!error && response.status == 200) {

        const $ = cheerio.load(response.data);
        var results = $("table").children().last();

        var orario = [];
        var nome_partita = [];
        var url = [];
        var icona = [];
        var categoria = [];
        var url_status = [];

        results.each(function (i, result) {
          $(result)
            .find("span")
            .each(function (index, element) {
              if ($(element).attr("class") != "t")
                icona = icona.concat([$(element).attr("class")]);

              if (
                $(element).text().replace(/\s+/g, " ").trim().includes(":") != ""
              ) {
                var getTime = new Date(
                  "2019-05-24T" +
                  $(element).text().replace(/\s+/g, " ").trim() +
                  ":18.850Z"
                );
                var addTime = new Date(
                  getTime.getTime() + 1000 * 60 * 60 * timezone
                );
                var time =
                  (addTime.getHours() < 10 ? "0" : "") +
                  addTime.getHours() +
                  ":" +
                  (getTime.getMinutes() < 10 ? "0" : "") +
                  getTime.getMinutes();
                orario = orario.concat(time);
              }
            });
        });

        results.each(function (i, result) {
          $(result)
            .find("td")
            .each(function (index, element) {
              if ($(element).attr("align") == "left")
                if ($(element).text().includes(":"))
                  categoria = categoria.concat([$(element).text().split(":")[0]]);
            });
        });

        results.each(function (i, result) {
          $(result)
            .find("a")
            .each(function (index, element) {
              if ($(element).text() != "") {
                nome_partita = nome_partita.concat([$(element).text()]);
                url = url.concat([$(element).attr("href")]);
                url_status = url_status.concat([
                  $(element).attr("href").replace(".php", "").replace("/", ""),
                ]);
              }
            });
        });

        var data_store = [];

        for (var j = 0; j < nome_partita.length; j++) {
          data_store[j] = {};

          data_store[j]["name"] = url[j].replace(/\s+/g, " ").trim();

          data_store[j]["name"] = nome_partita[j].replace(/\s+/g, " ").trim();

          if (orario[j] != undefined) data_store[j]["time"] = orario[j];
          else data_store[j]["time"] = "24/7";

          if (url[j].includes("http") == true) {
            data_store[j]["url"] = url[j];
          } else data_store[j]["url"] = siteurl.slice(0, -1) + url[j];

          if (url_dict.has(url_status[j]))
            data_store[j]["url_status"] = url_dict.get(url_status[j]);
          else data_store[j]["url_status"] = "0";

          if (icon_dict.has(icona[j]))
            data_store[j]["icon"] = icon_dict.get(icona[j]);
          else data_store[j]["icon"] = icona[j].replace(" ", "");

          if (categoria[j] != undefined) data_store[j]["class"] = categoria[j];
          else data_store[j]["class"] = "";
        }


        res.send(data_store);
      }
    })
    .catch(function (error) {
      console.log(error);
    });
});

exports = module.exports = app;
const server = app.listen(process.env.PORT || PORT, function () {});