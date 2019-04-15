package api.sql.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "misc_pages")
public class MiscPages {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "website_url")
	private String websiteUrl;
	
	@Column(name = "page_title")
	private String pageTitle;
	
	@Column(name = "page_content")
	private String pageContent;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getWebsiteUrl()
	{
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl)
	{
		this.websiteUrl = websiteUrl;
	}

	public String getPageTitle()
	{
		return pageTitle;
	}

	public void setPageTitle(String pageTitle)
	{
		this.pageTitle = pageTitle;
	}

	public String getPageContent()
	{
		return pageContent;
	}

	public void setPageContent(String pageContent)
	{
		this.pageContent = pageContent;
	}

	@Override
	public String toString()
	{
		return "MiscPages [id=" + id + ", websiteUrl=" + websiteUrl + ", pageTitle=" + pageTitle + ", pageContent=" + pageContent + "]";
	}
	
}
