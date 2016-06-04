import sys, requests, json, progressbar, time
from time import sleep
from threading import Thread


def progressbarFunction():
   bar = progressbar.ProgressBar(maxval=20, \
   widgets=[progressbar.Bar('=', '[', ']'), ' ', progressbar.Percentage()])
   bar.start()
   for i in xrange(20):
     bar.update(i+1)
     sleep(0.1)
   bar.finish()


t = Thread(target=progressbarFunction)

total_args = len(sys.argv)
article_query = ''
log_file_name = ''

# if total arguments = 1, no log or query is given. Create a default log file
# if total arguments = 2, consider the first one as log name, ask for query

if total_args == 1:
	print "Creating a default log file."
	log_file_name = 'wikisearchLog'
	print "Enter the search query."
	article_query = raw_input()
	article_query = article_query.replace(' ', '+').title()
elif total_args == 2:
	log_file_name = sys.argv[1]
	print "Enter the search query."
	article_query = raw_input()
	article_query = article_query.replace(' ', '+').title()
else:
	log_file_name = sys.argv[1]
	article_query = sys.argv[2:]
	article_query = '+'.join(map(str, article_query)).title()

url = "https://en.wikipedia.org/w/api.php?action=query&titles="
query = url + article_query + '&format=json'


t.start()
response = requests.get(query)

response_dict = json.loads(response.content)

response_dict = response_dict['query']['pages']

access_link = "http://en.wikipedia.org/wiki/index.html?curid="
all_article_links = ''

wikisearchLogFileObject = open(log_file_name,"a")

all_article_links = all_article_links + "\n" + access_link + response_dict.keys()[0]

wikisearchLogFileObject.write(all_article_links + "\n");
wikisearchLogFileObject.close()

t.join()
print "You can see your link in " + log_file_name + "\n"


