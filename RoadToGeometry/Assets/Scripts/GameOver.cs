using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GameOver : MonoBehaviour
{

    public GameObject gameOver;
    public GameObject scoreAndTasks;
    public AudioSource backgroungMusic;

    public bool isGameOver = false;

    // Start is called before the first frame update
    void Start()
    {
        gameOver.SetActive(false);
        scoreAndTasks.SetActive(true);
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetMouseButtonDown(0) && !isGameOver) 
        {
            EndGame();

        }
    }

    public void EndGame()
    {
        isGameOver = true;
        EventManager.Instance.GameOver();
        scoreAndTasks.SetActive(false);

        Vector3 previousPosition = gameOver.transform.position;
        Quaternion previousRotation = gameOver.transform.rotation;
        Vector3 playerPosition = transform.position;

        gameOver.transform.SetPositionAndRotation(
            new Vector3(previousPosition.x, previousPosition.y, playerPosition.z),
            previousRotation);
        gameOver.SetActive(true);

        backgroungMusic.Stop();

    }
}
