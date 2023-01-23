using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Pointer : MonoBehaviour
{
    private const string buttonTag = "Button";
    private const string toggleTag = "Toggle";
    private GameObject objectInSight = null;
    private Color normalColor = Color.black;
    private Color activeColor = new Color(0.54f, 0.32f, 0.61f, 1);
    private Color activeColor2 = Color.white;

    public GameObject menu;
    public GameObject settings;

    // Update is called once per frame
    void Update()
    {
        RaycastHit hit;
        
        if (Physics.Raycast(Camera.main.transform.position, Camera.main.transform.forward, out hit, Mathf.Infinity))
        {
            transform.position = hit.point;

            if (objectInSight != hit.transform.gameObject)
            {
                ChangeColor(normalColor, normalColor);
                objectInSight = hit.transform.gameObject;
                ChangeColor(activeColor, activeColor2);
            }
            else if (objectInSight.tag != buttonTag && objectInSight.tag != toggleTag)
            {
                ChangeColor(normalColor, normalColor);
                objectInSight = null;
            }
        }
        else
        {
            ChangeColor(normalColor, normalColor);
            objectInSight = null;
        }

        if (Input.GetMouseButtonDown(0))
        {
            OnClick();
        }
    }

    private void OnClick()
    {
        if (objectInSight != null && objectInSight.tag == buttonTag)
        {
            OnButtonClick();
        }
        else if (objectInSight != null && objectInSight.tag == toggleTag)
        {
            OnToggleClick();
        }
    }

    private void ChangeColor(Color color, Color color2)
    {
        if (objectInSight == null) return;

        if (objectInSight.tag == buttonTag)
        {
            ChangeButtonColor(color);
        }
        else if (objectInSight.tag == toggleTag)
        {
            ChangeToggleColor(color2);
        }
    }

    private void ChangeButtonColor(Color color)
    {
        if (objectInSight.TryGetComponent<Image>(out Image img))
        {
            img.color = color;
        }
    }

    private void ChangeToggleColor(Color color)
    {
        ColorBlock cb = objectInSight.GetComponent<Toggle>().colors;
        cb.normalColor = color;
        objectInSight.GetComponent<Toggle>().colors = cb;

        objectInSight.GetComponentInChildren<Text>().color = color;
    }

    private void OnButtonClick()
    {
        switch (objectInSight.name)
        {
            case "Level1Button":
                PlayLevel1();
                break;
            case "Level2Button":
                PlayLevel2();
                break;
            case "Level3Button":
                PlayLevel3();
                break;
            case "Level4Button":
                PlayLevel4();
                break;
            case "SettingsButton":
                OpenSettings();
                break;
            case "ExitButton":
                ExitApplication();
                break;
            case "SettingsBackButton":
                SettingsBackToMenu();
                break;

            default:
                break;

        }
    }

    private void OnToggleClick()
    {
        Toggle toggle = objectInSight.GetComponent<Toggle>();
        toggle.isOn = !toggle.isOn;
        SetUserPreferences();
    }

    public void SetUserPreferences()
    {
        if (objectInSight.GetComponent<Toggle>().isOn)
        {
            PlayerPrefs.SetInt(objectInSight.name, 0);
        } else
        {
            PlayerPrefs.SetInt(objectInSight.name, 1);
        }
    }

    private void PlayLevel1()
    {
        SceneManager.LoadScene("Level1");
    }

    private void PlayLevel2()
    {
        SceneManager.LoadScene("Level2");
    }

    private void PlayLevel3()
    {
        SceneManager.LoadScene("Level3");
    }

    private void PlayLevel4()
    {
        SceneManager.LoadScene("Level4");
    }


    private void OpenSettings()
    {
        menu.SetActive(false);
        settings.SetActive(true);
    }

    private void SettingsBackToMenu()
    {
        settings.SetActive(false);
        menu.SetActive(true);
    }

    private void ExitApplication()
    {
        Application.Quit();
    }
}
