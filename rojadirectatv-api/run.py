from flask import Flask
import requests
from bs4 import BeautifulSoup
import json

app = Flask(__name__)


icon_dict = {
    "am": "️️🅰️",
    "arg": "🇦🇷",
    "box": "🥊",
    "br": "🇧🇷",
    "cc2017": "🏆",
    "oro": "🏆",
    "caf": "🏆",
    "ch": "🏆",
    "ci": "🚲",
    "cl": "🇨🇱",
    "co": "🇨🇴",
    "de": "🇩🇪",
    "be": "🇩🇪",
    "ec": "🇪🇨",
    "en": "🇬🇧",
    "es": "🇪🇸",
    "f1": "🏁",
    "fr": "🇫🇷",
    "it": "🇮🇹",
    "mlb": "⚾️",
    "mx": "🇲🇽",
    "ca2019": "🏆",
    "nba": "🏀",
    "nl": "🇳🇱",
    "pe": "🇵🇪",
    "csuda": "🏆",
    "tr": "🇹🇷",
    "us": "🇺🇸",
    "el": "🏆",
    "uy": "🇺🇾",
    "wwe": "🤼‍",
    "suda": "🏆",
    "bkb": "🏀",
    "tenis": "🎾",
    "motogp": "🏍️",
    "soccer": "⚽",
    "icc": "🏆",
    "nfl": "🏈",
    "pt": "🇵🇹",
    "ar": "🇦🇷",
}


url_dict = {
    "bein": "1",
    "beinn": "1",
    "canal-31": "1",
    "canal1": "1",
    "canal2": "1",
    "directv": "1",
    "dplus": "1",
    "espn3": "1",
    "fox2": "1",
    "futbol": "1",
    "gol": "1",
    "santander": "1",
    "skip": "1",
    "canal-42": "1",
    "canal-30": "1",
    "canal-41": "1",
}


original_url = "https://www.rojadirectatvhd.org/"
host = original_url[8:][:-1]

headers = {
    "sec-ch-ua": '"Not_A Brand";v="8", "Chromium";v="120", "Google Chrome";v="120"',
    "sec-ch-ua-mobile": "?0",
    "sec-ch-ua-platform": '"Windows"',
    "Upgrade-Insecure-Requests": "1",
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
    "Sec-Fetch-Site": "cross-site",
    "Sec-Fetch-Mode": "navigate",
    "Sec-Fetch-User": "?1",
    "Sec-Fetch-Dest": "document",
    "host": host,
}


@app.route("/list")
def get_table_data():

    response = requests.request("GET", original_url, headers=headers)

    soup = BeautifulSoup(response.content, "html.parser")

    # Extract information from the first table
    first_table = soup.find("table")
    rows = first_table.select("tr")

    data_list = []

    for row in rows:
        time = row.select_one("td span.t").text.strip()[:-2]
        icon = row.select_one("td img")["src"].split("/")[-1][:-4]
        class_name = row.select_one("td img").get("alt", "")
        name = row.select_one("td a b").text.strip()
        url = original_url[:-1] + row.select_one("td a")["href"]

        url_status = (
            row.select_one("td a")["href"]
            .split("/")[-1][:-4]
            .replace("roja-direct-", "")
        )
        new_url_status = url_dict.get(url_status)
        if new_url_status == None:
            new_url_status = "0"

        new_icon = icon_dict.get(icon)
        if new_icon == None:
            new_icon = icon
            print(new_icon)
        data = {
            "name": name,
            "time": time,
            "url": url,
            "url_status": new_url_status,
            "icon": new_icon,
            "class": class_name,
        }

        data_list.append(data)

    # Create an array of JSON objects
    json_array = json.dumps(data_list, indent=2)

    return json_array


@app.route("/legal")
def get_legal():

    response = requests.request("GET", original_url + "legal.php", headers=headers)

    soup = BeautifulSoup(response.text, "html.parser")
    get = soup.find("div", class_="texto2")

    data_list = []

    data = {"text": str(get)}
    data_list.append(data)

    # Create an array of JSON objects
    json_array = json.dumps(data_list, indent=2)

    return json_array


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=1341, use_reloader=False)
