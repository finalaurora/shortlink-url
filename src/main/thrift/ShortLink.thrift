#@namespace scala modulex.service
include "ShortLinkDT.thrift"

service TUrlShortenerService {
     ShortLinkDT.TRedirectResult redirectLink(1: required string shortenCode)
     string shortenUrl(1: required string url)
}