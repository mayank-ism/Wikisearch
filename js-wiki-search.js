var cmdArguments = process.argv;
cmdArguments.shift();
cmdArguments.shift();
var logFileName = "";
var article_query = "";
if(cmdArguments.length <= 1) {
	if(cmdArguments.length == 0) {
		logFileName = "jsWikisearchLogFileDefault";
	} else {
		logFileName = cmdArguments[0];
	}

	console.log("Enter name of article to search: ");

    /*Figure out a way to terminate this.*/
    var stdin = process.openStdin();

    stdin.addListener("data", function(input) {
        article_query = input.toString().replace("\n","").split(" ").map(function(element) {
            return element.substring(0,1).toUpperCase() + element.substring(1,element.length).toLowerCase();
        }).join("+");

        mainWork(article_query,logFileName);
    });
} else {
	logFileName = cmdArguments[0];
	cmdArguments.shift();
	article_query = cmdArguments.map(function(element) {
		return element.substring(0,1).toUpperCase() + element.substring(1,element.length).toLowerCase();
	}).join("+");
    mainWork(article_query,logFileName);
}

function mainWork(article_query, logFileName) {
    var url = "https://en.wikipedia.org/w/api.php?action=query&titles=";
    var query = url + article_query + '&format=json';

    var http = require("https");
    http.get(query, function(res){
        var body = '';

        res.on('data', function(chunk){
            body += chunk;
        });

        res.on('end', function(){
            var wikiResponse = JSON.parse(body).query.pages;
            var access_link = "http://en.wikipedia.org/wiki/index.html?curid=";
            var allLinks = "";
            for(key in wikiResponse) {
                allLinks += (access_link + key + "\n");
            }

            var nodeFS = require("fs");
            /*Check this for appending*/
            nodeFS.open(logFileName,'a+',function(err, fd) {
                if(err) {
                    return console.error(err);
                }
                nodeFS.writeFile(logFileName,allLinks);
                nodeFS.close(fd);
            });
        });
    }).on('error', function(e){
          console.log("Got an error: ", e);
    });
}

